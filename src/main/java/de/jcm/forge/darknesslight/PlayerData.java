package de.jcm.forge.darknesslight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.jcm.forge.darknesslight.network.PacketSyncDarkPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerData implements IExtendedEntityProperties
{
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
	
	private boolean darkMage;
	private boolean darkness;
	
	private int energy;

	private HashMap<String, Integer> levels;
	private HashMap<String, Integer> cooldowns;
	
	public PlayerData()
	{
		this.levels=new HashMap<String, Integer>();
		this.cooldowns=new HashMap<String, Integer>();
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound extra = new NBTTagCompound();
		extra.setBoolean("darkMage", darkMage);
		extra.setBoolean("darkness", darkness);
		
		extra.setInteger("energy", energy);
		
		NBTTagCompound levels=new NBTTagCompound();
		for(int i=0;i<this.levels.size();i++)
		{
			String key=this.levels.keySet().toArray(new String[this.levels.size()])[i];
			levels.setInteger(key, this.levels.get(key));
		}
		
		extra.setTag("levels", levels);
		
		NBTTagCompound cooldowns=new NBTTagCompound();
		for(int i=0;i<this.cooldowns.size();i++)
		{
			String key=this.cooldowns.keySet().toArray(new String[this.cooldowns.size()])[i];
			cooldowns.setInteger(key, this.cooldowns.get(key));
		}			
		
		extra.setTag("cooldowns", cooldowns);
		
		compound.setTag(IDENTIFIER, extra);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound extra = compound.getCompoundTag(IDENTIFIER);
		darkMage=extra.getBoolean("darkMage");
		darkness = extra.getBoolean("darkness");
		
		energy=extra.getInteger("energy");
		
		NBTTagCompound levels=extra.getCompoundTag("levels");
		this.levels.clear();

		Set set=levels.func_150296_c();
		Iterator<?> it=set.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			int value=levels.getInteger(key);
			this.levels.put(key, value);
		}
		
		NBTTagCompound cooldowns=extra.getCompoundTag("cooldowns");
		this.cooldowns.clear();
		
		set=cooldowns.func_150296_c();
		it=set.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			int value=cooldowns.getInteger(key);
			this.cooldowns.put(key, value);
		}
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
		if (isServerSide()) DarknessVsLight.wrapper.sendToAll(new PacketSyncDarkPlayerData(player, this));
		else DarknessVsLight.wrapper.sendToServer(new PacketSyncDarkPlayerData(player, this));
	}
	
	public void requestSync()
	{
		DarknessVsLight.wrapper.sendToServer(new PacketSyncDarkPlayerData());
	}

	public boolean isDarkness()
	{
		return darkness;
	}

	public void setDarkness(boolean darkness)
	{
		this.darkness = darkness;
	}

	public boolean isDarkMage()
	{
		return darkMage;
	}

	public void setDarkMage(boolean darkMage)
	{
		this.darkMage = darkMage;
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
		return cooldowns.getOrDefault(skill.getName(), 0);
	}

	public void setCooldown(DarkSkills skill, int value)
	{
		if(!this.cooldowns.containsKey(skill.getName()))
			this.cooldowns.put(skill.getName(), value);
		this.cooldowns.replace(skill.getName(), value);
	}
	
	public int getCooldown(LightSkills skill)
	{
		return cooldowns.getOrDefault(skill.getName(), 0);
	}

	public void setCooldown(LightSkills skill, int value)
	{
		if(!this.cooldowns.containsKey(skill.getName()))
			this.cooldowns.put(skill.getName(), value);
		this.cooldowns.replace(skill.getName(), value);
	}
	
	public int getLevel(DarkSkills skill)
	{
		return levels.getOrDefault(skill.getName(), 0);
	}

	public void setLevel(DarkSkills skill, int value)
	{		
		if(!this.levels.containsKey(skill.getName()))
			this.levels.put(skill.getName(), value);
		this.levels.replace(skill.getName(), value);
	}
	
	public int getLevel(LightSkills skill)
	{
		return levels.getOrDefault(skill.getName(), 0);
	}

	public void setLevel(LightSkills skill, int value)
	{
		if(!this.levels.containsKey(skill.getName()))
			this.levels.put(skill.getName(), value);
		this.levels.replace(skill.getName(), value);
	}
	
	public void tickCooldowns()
	{
		for(int i=0;i<this.cooldowns.size();i++)
		{
			String key=this.cooldowns.keySet().toArray(new String[this.cooldowns.size()])[i];
			int value=this.cooldowns.get(key);
			if(value>0)
				this.cooldowns.replace(key, value-1);
		}
	}
}
