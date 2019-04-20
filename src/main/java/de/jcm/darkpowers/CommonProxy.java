package de.jcm.darkpowers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.jcm.darkpowers.PlayerData.DarkRole;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.entity.projectile.EntityBlackArrow;
import de.jcm.darkpowers.network.PacketClientEffect;
import de.jcm.darkpowers.network.PacketDispatcher;
import de.jcm.darkpowers.network.PacketSyncDarkPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy implements IGuiHandler
{
	private HashMap<UUID, PlayerData> temps=new HashMap<UUID, PlayerData>();
	public HashMap<Integer, Integer> blackArrowKills = new HashMap<>();

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		DarkPowers.logger.info(ID, new Exception("debug stack trace"));
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		DarkPowers.logger.info(ID, new Exception("debug stack trace"));
		return null;
	}

	public void preInit(FMLPreInitializationEvent e)
	{
		DarkPowers.logger.info("Common proxy pre init");
	}

	public void init(FMLInitializationEvent e)
	{
		DarkPowers.logger.info("Common proxy init");
	}

	public void postInit(FMLPostInitializationEvent e)
	{
		DarkPowers.logger.info("Common proxy post init");
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
					if(data.getRole()==DarkRole.MAGE || data.getRole()==DarkRole.DARKNESS)
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
							if(data.getEnergy()<data.getMaxEnergy())
							{
								int x=(int) player.posX;
								int y=(int) player.posY;
								int z=(int) player.posZ;

								int toAdd=15-player.worldObj.getBlockLightValue(x, y, z);

								data.setEnergy(data.getEnergy()+toAdd);

								if(toAdd>0)
									data.sync();
							}
						}
						if(time%2==0)
						{
							data.tickCooldowns();

							data.sync();
						}
						if(data.getRaytraceTicks()>0)
						{
							data.setRaytraceTicks(data.getRaytraceTicks()-1);
						}
					}
				}
			}
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
	public void onLivingDeath(LivingDeathEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			PlayerData data=PlayerData.get((EntityPlayer) event.entity);
			temps.put(event.entity.getUniqueID(), data);
		}
		if(!event.entityLiving.worldObj.isRemote)
		{
			if(event.source instanceof EntityDamageSource)
			{
				EntityDamageSource source = (EntityDamageSource) event.source;
				if(source.getSourceOfDamage() instanceof EntityBlackArrow)
				{
					EntityBlackArrow arrow = (EntityBlackArrow) source.getSourceOfDamage();

					int kills = blackArrowKills.getOrDefault(arrow.getEntityId(), 0)+1;
					blackArrowKills.put(arrow.getEntityId(), kills+1);

					if(kills>=6)
					{
						if(arrow.getOwner()!=null)
						{
							arrow.getOwner().triggerAchievement(DarkPowers.blackArrowMultikillAchievement);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(LivingAttackEvent event)
	{
		if(event.entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.entity;
			PlayerData data=PlayerData.get(player);

			int remainingHits = data.getRemainingBarrierHits();
			if(!event.source.canHarmInCreative() && remainingHits>0)
			{
				if(player.hurtResistantTime==0)
				{
					data.setRemainingBarrierHits(remainingHits-1);
					data.sync();
					player.hurtResistantTime = 20;

					player.worldObj.playSoundAtEntity(player, DarkPowers.MODID+":"+"darkShieldBlock", 1.0f, 1.0f);
				}

				DarkPowers.wrapper.sendToAllAround(new PacketClientEffect(player, ClientEffect.BARRIER_BLOCK, remainingHits),
					new TargetPoint(player.dimension, player.posX, player.posY, player.posZ,
						MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance()));

				event.setCanceled(true);
			}
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

	@SubscribeEvent
	public void onAchievement(AchievementEvent event)
	{
		if(!event.entityPlayer.worldObj.isRemote)
		{
			if(event.achievement==DarkPowers.blackArrowMultikillAchievement)
			{
				//TODO: give skin item
			}
		}
	}
}