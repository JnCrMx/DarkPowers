package de.jcm.darkpowers;

import de.jcm.darkpowers.block.DarkBlocks;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.network.PacketClientEffect;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;
import de.jcm.math.geo.vector.Vector2D;
import de.jcm.math.geo.vector.helpers.VectorAngle;
import de.jcm.util.Callback;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public enum DarkSkills
{
	//Name		 Sp     Type         CD   UC  EC   Callback
	JUMP		(false, Type.ACTIVE, 100,  1,  10, SkillCallbacks.JUMP_CALLBACK),
	TELEPORT	(false, Type.ACTIVE,   0,  8, 100, SkillCallbacks.TELEPORT_CALLBACK),
	PROTECTION	(false, Type.ACTIVE, 200, 32, 200, SkillCallbacks.PROTECTION_CALLBACK),
	DARK_DOME	(false, Type.ACTIVE, 150, 16, 150, SkillCallbacks.DARK_DOME_CALLBACK),
	EYE_DARKNESS(false, Type.PASSIVE,  0,  4,   0, player->true),	//no callback <-> simply check if skill is present
	BLACK_ARROW (false, Type.ACTIVE, 300, 24, 250, SkillCallbacks.BLACK_ARROW_CALLBACK)
	;

	public static enum Type
	{
		ACTIVE,
		PASSIVE,
		WEAPON
	}

	private static class SkillCallbacks
	{
		private static final Callback<Boolean, EntityPlayer> JUMP_CALLBACK = new Callback<Boolean, EntityPlayer>()
		{
			@Override
			public Boolean call(EntityPlayer player)
			{
				float yaw = player.rotationYaw;

				Vector2D vector = VectorAngle.createVector2D(-yaw, 5);

				player.motionX = vector.getX();
				player.motionY = 0.5;
				player.motionZ = vector.getY();

				S12PacketEntityVelocity packet = new S12PacketEntityVelocity(player);

				EntityPlayerMP playerMp = (EntityPlayerMP) player;
				WorldServer worldServer = (WorldServer) playerMp.worldObj;

				worldServer.getEntityTracker().func_151247_a(player, packet);
				playerMp.playerNetServerHandler.sendPacket(packet);

				return true;
			}
		};

		private static final Callback<Boolean, EntityPlayer> TELEPORT_CALLBACK = new Callback<Boolean, EntityPlayer>()
		{
			@Override
			public Boolean call(EntityPlayer player)
			{
				return false;
			}
		};

		private static final Callback<Boolean, EntityPlayer> PROTECTION_CALLBACK = new Callback<Boolean, EntityPlayer>()
		{
			@Override
			public Boolean call(EntityPlayer player)
			{
				PlayerData data=PlayerData.get(player);

				if(data.getRemainingBarrierHits()>=5)
					return false;

				data.setRemainingBarrierHits(5);
				data.sync();

				return true;
			}
		};

		private static final Callback<Boolean, EntityPlayer> DARK_DOME_CALLBACK = new Callback<Boolean, EntityPlayer>()
		{

			@Override
			public Boolean call(EntityPlayer player)
			{
				if(player.posY>246)
					return false;

				int x = (int)player.posX;
				int y = (int)player.posY+9;
				int z = (int)player.posZ;

				player.worldObj.setBlock(x, y, z, DarkBlocks.blockDarkDome);

				TileEntityDarkDome te = (TileEntityDarkDome) player.worldObj.getTileEntity(x, y, z);
				te.owner = player.getUniqueID();

				return true;
			}
		};

		private static final Callback<Boolean, EntityPlayer> BLACK_ARROW_CALLBACK = new Callback<Boolean, EntityPlayer>()
		{
			@Override
			public Boolean call(EntityPlayer player)
			{
				PlayerData data=PlayerData.get(player);
				if(data.getRaytraceTicks()==0)
				{
					data.setRaytraceTicks(20);

					DarkPowers.wrapper.sendTo(new PacketClientEffect(player, ClientEffect.RAYTRACE, 1000), (EntityPlayerMP) player);
				}
				return false;
			}
		};
	}

	private int unlockCost;
	private boolean special;
	private Type type;

	private int cooldown;
	private int energyCost;

	private Callback<Boolean, EntityPlayer> callback;

	private DarkSkills(boolean special, Type type, int cooldown, int unlockCost, int energyCost, Callback<Boolean, EntityPlayer> callback)
	{
		this.special=special;
		this.type=type;

		this.cooldown=cooldown;

		this.unlockCost=unlockCost;
		this.energyCost=energyCost;

		this.callback=callback;
	}

	public String getLocalizedName()
	{
		return I18n.format("skill."+name().toLowerCase()+".name", new Object[0]);
	}

	public Callback<Boolean, EntityPlayer> getCallback()
	{
		return callback;
	}

	public String getLocalizedDescription()
	{
		return I18n.format("skill."+name().toLowerCase()+".description", new Object[0]);
	}

	public String getTextureName()
	{
		return "textures/gui/skill_"+name().toLowerCase()+".png";
	}

	public ResourceLocation getTextureResource()
	{
		return new ResourceLocation(DarkPowers.MODID.toLowerCase(), getTextureName());
	}

	public boolean isSpecial()
	{
		return special;
	}

	public int getUnlockCost()
	{
		return unlockCost;
	}

	public Type getType()
	{
		return type;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public int getEnergyCost()
	{
		return energyCost;
	}
}
