package de.jcm.darkpowers.network;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.DarkSkills.Type;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.PlayerData.DarkRole;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

public class PacketDarkAction implements IMessage
{
	public static enum DarkAction
	{
		USE_SKILL,
		UNLOCK_SKILL,
		EQUIP_SKILL
	}

	private DarkAction action;
	private int[] arguments;
	
	public PacketDarkAction()
	{
		
	}
	
	public PacketDarkAction(DarkAction action, int... arguments)
	{
		this.action = action;
		this.arguments = arguments;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.action = DarkAction.values()[buf.readInt()];
		int argc = buf.readInt();
		this.arguments=new int[argc];
		for(int i=0;i<argc;i++)
		{
			arguments[i]=buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(action.ordinal());
		buf.writeInt(arguments.length);
		for(int i=0;i<arguments.length;i++)
		{
			buf.writeInt(arguments[i]);
		}
	}
	
	public static class Handler extends AbstractServerMessageHandler<PacketDarkAction>
	{
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketDarkAction message, MessageContext ctx)
		{			
			if(message.action == DarkAction.UNLOCK_SKILL)
			{
				int fragmentCount = 0;
				for(int i=0;i<player.inventory.mainInventory.length;i++)
				{
					ItemStack stack = player.inventory.mainInventory[i];
					if(stack!=null)
					{
						if(stack.getItem()==DarkPowers.itemDarkIngot)
						{
							fragmentCount+=stack.stackSize;
						}
					}
				}
				
				DarkSkills skill = DarkSkills.values()[message.arguments[0]];
				PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
				
				if((skill.isSpecial() && data.getRole()==DarkRole.DARKNESS) || !skill.isSpecial())
				{
					if(!data.getUnlocks().contains(skill))
					{
						if(fragmentCount>=skill.getUnlockCost())
						{
							if(skill.getType()==Type.ACTIVE)
							{
								for(int i=0;i<skill.getUnlockCost();i++)
									player.inventory.consumeInventoryItem(DarkPowers.itemDarkIngot);
								data.getUnlocks().add(skill);
								data.sync();
							}
							else if(skill.getType()==Type.PASSIVE)
							{
								if(skill.getCallback().call(player))
								{
									for(int i=0;i<skill.getUnlockCost();i++)
										player.inventory.consumeInventoryItem(DarkPowers.itemDarkIngot);
									data.getUnlocks().add(skill);
									data.sync();
								}
							}
						}
					}
				}
			}
			if(message.action == DarkAction.USE_SKILL)
			{
				PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
				DarkSkills skill = data.getSelectedSkills()[message.arguments[0]];
				if(skill.getType()==Type.ACTIVE)
				{
					if(data.getRole() == DarkRole.MAGE || data.getRole() == DarkRole.DARKNESS)
					{
						if(data.getUnlocks().contains(skill))
						{
							if(data.getEnergy() >= skill.getEnergyCost())
							{
								if(data.getCooldown(skill) == 0)
								{
									if(skill.getCallback().call(player))
									{
										data.setCooldown(skill, skill.getCooldown());
										data.setEnergy(data.getEnergy() - skill.getEnergyCost());
										data.sync();
									}
								}
							}
						}
					}
				}
			}
			if(message.action == DarkAction.EQUIP_SKILL)
			{
				PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
				int slot = message.arguments[0];
				DarkSkills skill = null;
				if(message.arguments[1]>=0 && message.arguments[1]<DarkSkills.values().length)
				{
					skill = DarkSkills.values()[message.arguments[1]];
				}
				
				if(data.getUnlocks().contains(skill) || skill==null)
				{
					if(skill==null || skill.getType()==Type.ACTIVE)
					{
						if(slot>=0 && slot<data.getSelectedSkills().length)
						{
							data.getSelectedSkills()[slot]=skill;
							data.sync();
						}
					}
					else if(skill!=null)
					{
						if(skill.getType()==Type.PASSIVE)
						{
							if(slot==1)
							{
								if(!data.getEnabledPassiveSkills().contains(skill))
								{
									data.getEnabledPassiveSkills().add(skill);
									data.sync();
								}
							}
							else if(slot==0)
							{
								if(data.getEnabledPassiveSkills().contains(skill))
								{
									data.getEnabledPassiveSkills().remove(skill);
									data.sync();
								}
							}
						}
					}
				}
			}
			
			return null;
		}
	}
}
