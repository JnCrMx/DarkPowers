package de.jcm.darkpowers.network;

import de.jcm.darkpowers.ClientProxy;
import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.entity.projectile.EntityBlackArrow;
import de.jcm.darkpowers.network.PacketDarkAction.DarkAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

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
			if(message.effect == ClientEffect.RAYTRACE)
			{
				MovingObjectPosition result = player.rayTrace(message.argument, 0);
				if(result!=null)
				{
					if(result.typeOfHit==MovingObjectType.BLOCK)
					{
						DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.RAYTRACE_BLOCK,
							result.blockX, result.blockY, result.blockZ, result.sideHit));
					}
					if(result.typeOfHit==MovingObjectType.ENTITY)	//Maybe this never happens
					{
						DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.RAYTRACE_BLOCK,
							(int)result.entityHit.posX, (int)result.entityHit.posY, (int)result.entityHit.posZ, -1));
					}
					if(result.typeOfHit==MovingObjectType.MISS)
					{
						DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.RAYTRACE_MISS));
					}
				}
			}
			if(message.effect == ClientEffect.BLACK_ARROW)
			{
				if(message.entity==player.getEntityId())
				{
					if(message.argument==-1)
					{
						Minecraft.getMinecraft().entityRenderer.deactivateShader();
					}
					else
					{
						Minecraft mc = Minecraft.getMinecraft();
						EntityRenderer renderer = mc.entityRenderer;
						if(renderer.isShaderActive())
						{
							renderer.theShaderGroup.deleteShaderGroup();
						}
						try
						{
							renderer.theShaderGroup =
									new ShaderGroup(mc.renderEngine,
									mc.getResourceManager(),
									mc.getFramebuffer(),
									new ResourceLocation("shaders/post/phosphor.json"));
							renderer.theShaderGroup.createBindFramebuffers(
									mc.displayWidth, mc.displayHeight);
						}
						catch(JsonException e1)
						{
							e1.printStackTrace();
						}

						proxy.blackArrowCamera = (EntityBlackArrow) player.worldObj.getEntityByID(message.argument);
					}
				}
				Entity entity = player.worldObj.getEntityByID(message.entity);
				if(entity instanceof EntityPlayer)
				{
					if(message.argument==-1)
					{
						PlayerData data = PlayerData.get((EntityPlayer)entity);
						data.setClientEffect(null);
					}
					else
					{
						PlayerData data = PlayerData.get((EntityPlayer)entity);
						data.setClientEffect(ClientEffect.BLACK_ARROW);
					}
				}
			}

			return null;
		}
	}
}
