package de.jcm.darkpowers.network;

import de.jcm.darkpowers.ClientProxy;
import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.client.ClientEffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

//TODO: Implement
public class PacketClientEffect implements IMessage
{
	private int entity;
	private ClientEffect effect;
	private int argument;
	
	public PacketClientEffect()
	{
		
	}
	
	public PacketClientEffect(Entity entity, ClientEffect effect, int argument)
	{
		this.entity = entity.getEntityId();
		this.effect = effect;
		this.argument = argument;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entity = buf.readInt();
		this.effect = ClientEffect.values()[buf.readInt()];
		this.argument = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entity);
		buf.writeInt(this.effect.ordinal());
		buf.writeInt(argument);
	}
	
	@SideOnly(Side.CLIENT)
	public static class Handler extends AbstractClientMessageHandler<PacketClientEffect>
	{
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketClientEffect message, MessageContext ctx)
		{
			ClientProxy proxy = (ClientProxy) DarkPowers.commonProxy;
			
			if(message.effect == ClientEffect.DARKNESS)
			{
				proxy.renderDark = message.argument == 1;
			}
			if(message.effect == ClientEffect.BARRIER_BLOCK)
			{
				PlayerData data = (PlayerData) player.worldObj.getEntityByID(message.entity).getExtendedProperties(PlayerData.IDENTIFIER);
				data.setClientEffect(ClientEffect.BARRIER_BLOCK);
				data.setClientEffectTicks(20);
			}
			
			return null;
		}
	}
}
