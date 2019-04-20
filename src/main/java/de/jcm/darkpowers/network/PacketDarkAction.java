package de.jcm.darkpowers.network;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.DarkSkills.Type;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.PlayerData.DarkRole;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.entity.projectile.EntityBlackArrow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

public class PacketDarkAction implements IMessage
{
	public static enum DarkAction
	{
		USE_SKILL,
		UNLOCK_SKILL,
		EQUIP_SKILL,
		RAYTRACE_BLOCK,
		RAYTRACE_MISS
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
				PlayerData data = PlayerData.get(player);

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
				PlayerData data = PlayerData.get(player);
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
				PlayerData data = PlayerData.get(player);
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
			if(message.action==DarkAction.RAYTRACE_BLOCK)
			{
				PlayerData data = PlayerData.get(player);
				if(data.getRaytraceTicks()>0)
				{
					data.setRaytraceTicks(0);
					double x = message.arguments[0]+0.5;
					double y = message.arguments[1];
					double z = message.arguments[2]+0.5;
					int face = message.arguments[3];

					switch(face)
					{
						case 0:
							y--;
							break;
						case 1:
							y++;
							break;
						case 2:
							z--;
							break;
						case 3:
							z++;
							break;
						case 4:
							x--;
							break;
						case 5:
							x++;
							break;
						default:
							break;
					}

					EntityBlackArrow arrow = new EntityBlackArrow(player.worldObj, player, x, y, z);
					player.worldObj.spawnEntityInWorld(arrow);

					DarkPowers.wrapper.sendToAllAround(new PacketClientEffect(player, ClientEffect.BLACK_ARROW, arrow.getEntityId()),
						new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 100));

					data.setEnergy(data.getEnergy()-DarkSkills.BLACK_ARROW.getEnergyCost());
					data.setCooldown(DarkSkills.BLACK_ARROW, DarkSkills.BLACK_ARROW.getCooldown());
					data.sync();
				}
			}
			if(message.action==DarkAction.RAYTRACE_MISS)
			{
				PlayerData data = PlayerData.get(player);
				if(data.getRaytraceTicks()>0)
				{
					data.setRaytraceTicks(0);
				}
			}

			return null;
		}
	}
}
