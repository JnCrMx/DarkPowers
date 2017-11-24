package de.jcm.forge.darknesslight.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketDispatcher
{
	private static byte packetId = 0;
	
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(DarknessVsLight.MODID);
	
	public static final void registerPackets()
	{
		PacketDispatcher.registerBiMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class);
	}
	
	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		dispatcher.registerMessage(messageHandler, requestMessageType, packetId++, side);
	}
	
	public static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass)
	{
		Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}
	
	public static void sendToAll(IMessage message)
	{
		dispatcher.sendToAll(message);
	}
	
	public static void sendTo(IMessage message, EntityPlayerMP player)
	{
		dispatcher.sendTo(message, player);
	}
	
	public static void sendToAllAround(IMessage message, TargetPoint point)
	{
		dispatcher.sendToAllAround(message, point);
	}
	
	public static void sendToDimension(IMessage message, int dimensionId)
	{
		dispatcher.sendToDimension(message, dimensionId);
	}
	
	public static void sendToServer(IMessage message)
	{
		dispatcher.sendToServer(message);
	}
	
	public static final void registerBiMessage(Class handlerClass, Class messageClass)
	{
		if (AbstractBiMessageHandler.class.isAssignableFrom(handlerClass))
		{
			PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId, Side.CLIENT);
			PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, Side.SERVER);
		}
		else
		{
			throw new IllegalArgumentException("Cannot register " + handlerClass.getName() + " on both sides - must extend AbstractBiMessageHandler!");
		}
	}
	
}
