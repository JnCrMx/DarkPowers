package de.jcm.darkpowers;

import de.jcm.darkpowers.network.PacketSyncDarkPlayerData;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class ServerProxy extends CommonProxy
{
	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		
		DarkPowers.logger.info("Server proxy init");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		
		DarkPowers.logger.info("Server proxy pre init");
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
		
		DarkPowers.logger.info("Server proxy post init");
		DarkPowers.wrapper.registerMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class, 0, Side.SERVER);
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}
}
