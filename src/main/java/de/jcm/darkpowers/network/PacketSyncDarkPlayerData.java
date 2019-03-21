package de.jcm.darkpowers.network;

import de.jcm.darkpowers.PlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

public class PacketSyncDarkPlayerData implements IMessage
{
	private NBTTagCompound nbt;
	
	private boolean request;

	
	public PacketSyncDarkPlayerData()
	{
		this.request = true;
	}
	
	public PacketSyncDarkPlayerData(EntityPlayer player, PlayerData data)
	{
		nbt=new NBTTagCompound();
		data.saveNBTData(nbt);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.nbt = ByteBufUtils.readTag(buf);
		
		this.request = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbt);
		
		buf.writeBoolean(request);
	}
	
	public static class Handler extends AbstractBiMessageHandler<PacketSyncDarkPlayerData>
	{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketSyncDarkPlayerData message, MessageContext ctx)
		{			
			PlayerData data=PlayerData.get(player);
			
			data.loadNBTData(message.nbt);
			
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketSyncDarkPlayerData message, MessageContext ctx)
		{			
			if(message.request)
			{				
				PlayerData data=PlayerData.get(player);
				PacketSyncDarkPlayerData answer=new PacketSyncDarkPlayerData(player, data);
				
				return answer;
			}
			
			return null;
		}
		
	}
}
