package de.jcm.darkpowers.network;

import de.jcm.darkpowers.DarkPowers;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.relauncher.Side;

public class PacketDispatcher
{
	private static byte packetId = 0;
	
	public static final void registerPackets()
	{
		PacketDispatcher.registerBiMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class);
		PacketDispatcher.registerMessage(PacketClientEffect.Handler.class, PacketClientEffect.class, Side.CLIENT);
		PacketDispatcher.registerMessage(PacketDarkAction.Handler.class, PacketDarkAction.class, Side.SERVER);
	}
	
	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		DarkPowers.wrapper.registerMessage(messageHandler, requestMessageType, packetId++, side);
	}
	
	public static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass)
	{
		Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
		DarkPowers.wrapper.registerMessage(handlerClass, messageClass, packetId++, side);
	}
	
	public static void sendToAll(IMessage message)
	{
		DarkPowers.wrapper.sendToAll(message);
	}
	
	public static void sendTo(IMessage message, EntityPlayerMP player)
	{
		DarkPowers.wrapper.sendTo(message, player);
	}
	
	public static void sendToAllAround(IMessage message, TargetPoint point)
	{
		DarkPowers.wrapper.sendToAllAround(message, point);
	}
	
	public static void sendToDimension(IMessage message, int dimensionId)
	{
		DarkPowers.wrapper.sendToDimension(message, dimensionId);
	}
	
	public static void sendToServer(IMessage message)
	{
		DarkPowers.wrapper.sendToServer(message);
	}
	
	public static final void registerBiMessage(Class handlerClass, Class messageClass)
	{
		if (AbstractBiMessageHandler.class.isAssignableFrom(handlerClass))
		{
			DarkPowers.wrapper.registerMessage(handlerClass, messageClass, packetId, Side.CLIENT);
			DarkPowers.wrapper.registerMessage(handlerClass, messageClass, packetId++, Side.SERVER);
		}
		else
		{
			throw new IllegalArgumentException("Cannot register " + handlerClass.getName() + " on both sides - must extend AbstractBiMessageHandler!");
		}
	}
	
}
