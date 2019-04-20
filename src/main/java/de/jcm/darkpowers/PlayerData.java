package de.jcm.darkpowers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.jcm.darkpowers.DarkSkills.Type;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.network.PacketSyncDarkPlayerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.IExtendedEntityProperties;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerData implements IExtendedEntityProperties
{
	public static enum DarkRole
	{
		NONE,
		SERVANT,
		MAGE,
		DARKNESS
	}
	
	public static final String IDENTIFIER = "DarkLightPlayerData";

	public static PlayerData get(EntityPlayer player)
	{
		return (PlayerData) player.getExtendedProperties(IDENTIFIER);
	}

	public static void register(EntityPlayer player)
	{
		player.registerExtendedProperties(IDENTIFIER, new PlayerData());
	}

	private World world;
	private EntityPlayer player;
	
	private DarkRole role;
	
	private int energy;
	private int maxEnergy;

	private ArrayList<DarkSkills> unlocks;
	private HashMap<DarkSkills, Integer> cooldowns;
	
	private DarkSkills[] selectedSkills = new DarkSkills[4];
	
	private int remainingBarrierHits;
	private ArrayList<DarkSkills> passives;
	
	//Sending this via the packet might be inefficient, use PacketClientEffect
	@SideOnly(Side.CLIENT)
	private ClientEffect clientEffect;
	@SideOnly(Side.CLIENT)
	private int clientEffectTicks;
	
	private int raytraceTicks;

	@SideOnly(Side.CLIENT)
	public ClientEffect getClientEffect()
	{
		return clientEffect;
	}

	@SideOnly(Side.CLIENT)
	public void setClientEffect(ClientEffect clientEffect)
	{
		this.clientEffect = clientEffect;
	}

	@SideOnly(Side.CLIENT)
	public int getClientEffectTicks()
	{
		return clientEffectTicks;
	}

	@SideOnly(Side.CLIENT)
	public void setClientEffectTicks(int clientEffectTicks)
	{
		this.clientEffectTicks = clientEffectTicks;
	}
	
	public int getRaytraceTicks()
	{
		return raytraceTicks;
	}

	public void setRaytraceTicks(int raytraceTicks)
	{
		this.raytraceTicks = raytraceTicks;
	}

	public PlayerData()
	{
		this.unlocks=new ArrayList<>();
		this.cooldowns=new HashMap<>();
		this.role=DarkRole.NONE;
		this.passives=new ArrayList<>();
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound extra = new NBTTagCompound();
		extra.setString("role", role.name());

		extra.setInteger("energy", energy);
		extra.setInteger("maxEnergy", maxEnergy);
		
		StringBuilder unlocks=new StringBuilder();
		for(int i=0;i<this.unlocks.size();i++)
		{
			DarkSkills skill = this.unlocks.get(i);
			unlocks.append(skill.name());
			unlocks.append(',');
		}
		if(unlocks.length()>0)
			unlocks.deleteCharAt(unlocks.length()-1);
		extra.setString("unlocks", unlocks.toString());
		
		NBTTagCompound cooldowns=new NBTTagCompound();
		for(int i=0;i<this.cooldowns.size();i++)
		{
			DarkSkills key=this.cooldowns.keySet().toArray(new DarkSkills[this.cooldowns.size()])[i];
			cooldowns.setInteger(key.name(), this.cooldowns.get(key));
		}			
		
		extra.setTag("cooldowns", cooldowns);
		
		for(int i=0;i<selectedSkills.length;i++)
		{
			DarkSkills skill = selectedSkills[i];
			extra.setString("skill"+i, skill!=null?skill.name():"null");
		}
		
		extra.setInteger("remainingBarrierHits", remainingBarrierHits);
		
		StringBuilder passives=new StringBuilder();
		for(int i=0;i<this.passives.size();i++)
		{
			DarkSkills skill = this.passives.get(i);
			if(skill.getType()==Type.PASSIVE)
			{
				passives.append(skill.name());
				passives.append(',');
			}
			else
			{
				DarkPowers.logger.warn("Found non passive skill in passive list while saving: "+skill);
				this.passives.remove(skill);
			}
		}
		if(passives.length()>0)
			passives.deleteCharAt(passives.length()-1);
		extra.setString("passives", passives.toString());
		
		compound.setTag(IDENTIFIER, extra);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound extra = compound.getCompoundTag(IDENTIFIER);
		String role = extra.getString("role");
		try
		{
			this.role = DarkRole.valueOf(role);
		}
		catch (IllegalArgumentException e) 
		{
			DarkPowers.logger.warn("Illegal role given in data of player "+player.getUniqueID()+": "+role);
			this.role = DarkRole.NONE;
		}
		
		energy=extra.getInteger("energy");
		maxEnergy=extra.getInteger("maxEnergy");
		
		String unlocks=extra.getString("unlocks");
		this.unlocks.clear();
		for(String unlock : unlocks.split(","))
		{
			if(!unlock.isEmpty())
			{
				try
				{
					this.unlocks.add(DarkSkills.valueOf(unlock));
				}
				catch(IllegalArgumentException e)
				{
					DarkPowers.logger.warn("Illegal unlock given in data of player "+player.getUniqueID()+": "+unlock);
				}
			}
		}
		
		NBTTagCompound cooldowns=extra.getCompoundTag("cooldowns");
		this.cooldowns.clear();
		
		Set set=cooldowns.func_150296_c();
		Iterator<String> it=set.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			int value=cooldowns.getInteger(key);
			this.cooldowns.put(DarkSkills.valueOf(key), value);
		}
		
		for(int i=0;i<selectedSkills.length;i++)
		{
			String skill = extra.getString("skill"+i);
			if(!skill.isEmpty() && !skill.equals("null"))
			{
				selectedSkills[i]=DarkSkills.valueOf(skill);
			}
			else
			{
				selectedSkills[i]=null;
			}
		}
		
		remainingBarrierHits = extra.getInteger("remainingBarrierHits");
		
		String passives=extra.getString("passives");
		this.passives.clear();
		for(String passive : passives.split(","))
		{
			if(!passive.isEmpty())
			{
				try
				{
					DarkSkills skill = DarkSkills.valueOf(passive);
					if(skill.getType()==Type.PASSIVE)
					{
						this.passives.add(skill);
					}
					else
					{
						DarkPowers.logger.warn("Found non passive skill in passive list while loading: "+skill);
					}
				}
				catch(IllegalArgumentException e)
				{
					DarkPowers.logger.warn("Illegal passive skill given in data of player "+player.getUniqueID()+": "+passive);
				}
			}
		}
	}
	
	public ArrayList<DarkSkills> getEnabledPassiveSkills()
	{
		return passives;
	}

	@Override
	public void init(Entity entity, World world)
	{
		this.world = world;
		if (entity instanceof EntityPlayer)
			this.player = (EntityPlayer) entity;
		else
			throw new IllegalArgumentException("Entity not a player");
	}
	
	public boolean isServerSide()
	{
		return this.player instanceof EntityPlayerMP;
	}
	
	public void sync()
	{
		if (isServerSide()) DarkPowers.wrapper.sendToAll(new PacketSyncDarkPlayerData(player, this));
		else DarkPowers.wrapper.sendToServer(new PacketSyncDarkPlayerData(player, this));
	}
	
	public void requestSync()
	{
		DarkPowers.wrapper.sendToServer(new PacketSyncDarkPlayerData());
	}

	public DarkRole getRole()
	{
		return role;
	}

	public void setRole(DarkRole role)
	{
		this.role = role;
	}

	public int getEnergy()
	{
		return energy;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public int getCooldown(DarkSkills skill)
	{
		return cooldowns.getOrDefault(skill, 0);
	}

	public void setCooldown(DarkSkills skill, int value)
	{
		if(!this.cooldowns.containsKey(skill))
			this.cooldowns.put(skill, value);
		this.cooldowns.replace(skill, value);
	}
	public void tickCooldowns()
	{
		for(int i=0;i<this.cooldowns.size();i++)
		{
			DarkSkills key=this.cooldowns.keySet().toArray(new DarkSkills[this.cooldowns.size()])[i];
			int value=this.cooldowns.get(key);
			if(value>0)
				this.cooldowns.replace(key, value-1);
		}
	}

	public ArrayList<DarkSkills> getUnlocks()
	{
		return unlocks;
	}

	public DarkSkills[] getSelectedSkills()
	{
		return selectedSkills;
	}

	public int getMaxEnergy()
	{
		return maxEnergy;
	}

	public void setMaxEnergy(int maxEnergy)
	{
		this.maxEnergy = maxEnergy;
	}

	public int getRemainingBarrierHits()
	{
		return remainingBarrierHits;
	}

	public void setRemainingBarrierHits(int remainingBarrierHits)
	{
		this.remainingBarrierHits = remainingBarrierHits;
	}
}
