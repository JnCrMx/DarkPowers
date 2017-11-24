package de.jcm.forge.darknesslight;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.jcm.forge.darknesslight.network.PacketSyncDarkPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy
{
	@Override
	public void init(FMLInitializationEvent e)
	{
		System.out.println("Server proxy init");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		System.out.println("Server proxy pre init");
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		System.out.println("Server proxy post init");
		DarknessVsLight.wrapper.registerMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class, 0, Side.SERVER);
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}
}
