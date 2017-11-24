package de.jcm.forge.darknesslight;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jcm.forge.darknesslight.network.PacketDispatcher;
import de.jcm.forge.darknesslight.network.PacketSyncDarkPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public abstract class CommonProxy implements IGuiHandler
{
	private HashMap<UUID, PlayerData> temps=new HashMap<UUID, PlayerData>();

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null)
		{
			if (ID == 1)
			{
				
			}
		}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null)
		{
			if (ID == 1)
			{
				
			}
		}
		return null;
	}
	
	public void preInit(FMLPreInitializationEvent e)
	{
		System.out.println("Server proxy pre init");
	}
	
	public void init(FMLInitializationEvent e)
	{
		System.out.println("Server proxy init");
	}
	
	public void postInit(FMLPostInitializationEvent e)
	{
		System.out.println("Server proxy post init");
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = false)
	public void onEntityConstruct(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.getExtendedProperties(PlayerData.IDENTIFIER) == null)
			{
				PlayerData data=new PlayerData();
				player.registerExtendedProperties(PlayerData.IDENTIFIER, data);
				data.requestSync();
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent event)
	{
		if ((Minecraft.getMinecraft().isSingleplayer() && event.side==Side.CLIENT) || event.side == Side.SERVER) if (event.phase == Phase.START)
		{
			List list=MinecraftServer.getServer().getEntityWorld().playerEntities;
			
			for (Object object : list)
			{
				EntityPlayerMP player=(EntityPlayerMP) object;
				if(player!=null)
				{
					PlayerData data=PlayerData.get(player);
					if(data.isDarkMage() || data.isDarkness())
					{							
						if(data.getEnergy()<10)
						{
							player.addPotionEffect(new PotionEffect(9,100,0));
						}
						if(data.getEnergy()<20)
						{
							player.addPotionEffect(new PotionEffect(2,100,4));
						}
						if(data.getEnergy()<30)
						{
							player.addPotionEffect(new PotionEffect(18,100,4));
						}
						
						long time=player.worldObj.getTotalWorldTime();
						if(time%20==0)
						{
							if(data.getEnergy()<Constants.MAX_ENERGY)
							{
								int x=(int) player.posX;
								int y=(int) player.posY;
								int z=(int) player.posZ;
								
								int toAdd=15-player.worldObj.getBlockLightValue(x, y, z);
								
								data.setEnergy(data.getEnergy()+toAdd);
								//System.out.println(data.getEnergy()+"/"+Constants.MAX_ENERGY);
								
								if(toAdd>0)
									data.sync();
							}
						}
						if(time%2==0)
						{
							data.tickCooldowns();

							data.sync();
						}
					}
				}
			}
			
			/*EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			NBTTagCompound tag = player.getEntityData();
			NBTTagCompound cooldowns = tag.getCompoundTag("skillCooldowns");
			if (cooldowns == null)
			{
				tag.setTag("skillCooldowns", new NBTTagCompound());
				cooldowns = tag.getCompoundTag("skillCooldowns");
			}
			
			int cooldownFlash = cooldowns.getInteger("flash");
			if (cooldownFlash > 0)
			{
				cooldownFlash = cooldownFlash - 1;
				cooldowns.setInteger("flash", cooldownFlash);
			}
			
			int cooldownJump = cooldowns.getInteger("jump");
			if (cooldownJump > 0)
			{
				cooldownJump = cooldownJump - 1;
				cooldowns.setInteger("jump", cooldownJump);
			}
			
			tag.setTag("skillCooldowns", cooldowns);
			
			if(true)
			{
				PlayerData data=PlayerData.get(player);
				if(data.isDarkMage() || data.isDark())
				{
					if(data.getEnergy()<10)
					{
						player.addPotionEffect(new PotionEffect(9,100,0));
					}
					if(data.getEnergy()<20)
					{
						player.addPotionEffect(new PotionEffect(2,100,0));
					}
					if(data.getEnergy()<30)
					{
						player.addPotionEffect(new PotionEffect(18,100,0));
					}
				}
				System.out.println(player.worldObj.getTotalWorldTime());
			}*/
		}
	}
	
	public abstract EntityPlayer getPlayerEntity(MessageContext ctx);
	
	@SubscribeEvent
	public void onPlayerLeft(PlayerLoggedOutEvent event)
	{
		PlayerData data=PlayerData.get(event.player);
		PacketSyncDarkPlayerData pack=new PacketSyncDarkPlayerData(event.player, data);
		PacketDispatcher.sendToAll(pack);
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		PacketSyncDarkPlayerData pack=new PacketSyncDarkPlayerData();
		PacketDispatcher.sendToServer(pack);
	}
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			PacketSyncDarkPlayerData pack=new PacketSyncDarkPlayerData();
			PacketDispatcher.sendToServer(pack);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDies(LivingDeathEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			PlayerData data=PlayerData.get((EntityPlayer) event.entity);
			temps.put(event.entity.getUniqueID(), data);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawns(PlayerRespawnEvent event)
	{
		
		if(temps.containsKey(event.player.getUniqueID()))
		{
			PlayerData data=temps.get(event.player.getUniqueID());
			PlayerData data2=PlayerData.get(event.player);
			
			NBTTagCompound tag=new NBTTagCompound();
			data.saveNBTData(tag);
			data2.loadNBTData(tag);
			
			data2.sync();
			
			temps.remove(event.player.getUniqueID());
		}
	}
}