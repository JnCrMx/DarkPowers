package de.jcm.darkpowers.tileentity;

import java.util.List;
import java.util.UUID;

import de.jcm.darkpowers.ClientProxy;
import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.util.PlayerReferenceHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityDarkDome extends TileEntity
{
	public double radius = 5;
	public int ticksToExist = 200;
	public UUID owner;
	
	public TileEntityDarkDome()
	{
		
	}
	
	@Override
	public void updateEntity()
	{
		ticksToExist--;
		markDirty();
		if(ticksToExist<=0 && !worldObj.isRemote)
		{
			this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}
		
		if(worldObj.isRemote)
		{
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if(!player.getUniqueID().equals(owner))	//The owner of a dome should be able to see normally in it. 
			{
				if(checkInDome(this, player))
				{
					ClientProxy proxy = (ClientProxy) DarkPowers.commonProxy;
					proxy.renderDome = this;
				}
			}
		}
		else
		{
			EntityPlayer player = PlayerReferenceHelper.findPlayer(owner);
			if(checkInDome(this, player))
			{
				player.addPotionEffect(new PotionEffect(Potion.damageBoost.id,	 20, 2, true));
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id,	 20, 2, true));
				player.addPotionEffect(new PotionEffect(Potion.resistance.id,	 20, 2, true));
				player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, 20, 2, true));	//Saturation
			}
			
			List<?> entites = worldObj.getEntitiesWithinAABB(EntityCreature.class,
				AxisAlignedBB.getBoundingBox(xCoord-radius, yCoord-10, zCoord-radius, 
					xCoord+radius, yCoord, zCoord+radius));
			for(Object object : entites)
			{
				if(object instanceof EntityCreature)
				{
					EntityCreature creature = (EntityCreature) object;
					if(checkInDome(this, creature))
					{
						if(creature.getAttackTarget()!=null)
						{
							if(creature.getAttackTarget().getUniqueID().equals(owner))	//only protect owner
							{
								creature.setAttackTarget(null);	//does not work perfectly, but slows some entities a lot
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean checkInDome(TileEntityDarkDome dome, Entity entity)
	{
		if(dome==null || entity==null || dome.worldObj==null || entity.worldObj!=dome.worldObj)
		{
			return false;
		}
		
		World world = dome.worldObj;
		double rx = dome.xCoord+0.5-entity.posX;
		double ry = dome.yCoord-(entity.posY-entity.yOffset);
		double rz = dome.zCoord+0.5-entity.posZ;
			
		double r = dome.radius;
			
		double h = 1-(ry)/(r*2);
		if(h<=1 && h>=0)
		{			
			double a = Math.acos(h);
			double r3 = Math.sin(a)*r;
				
			//System.out.println("h="+h+" a="+Math.toDegrees(a)+" r3="+r3+" cos(a)*r="+Math.cos(a));
				
			double dist = Math.sqrt(rx*rx+rz*rz);
				
			if(dist<r3)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		
		tag.setDouble("radius", radius);
		tag.setInteger("ticksToExist", ticksToExist);
		if(owner!=null)
		{
			tag.setString("owner", owner.toString());
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		radius = tag.getDouble("radius");
		ticksToExist = tag.getInteger("ticksToExist");
		
		String owner = tag.getString("owner");
		if(owner!=null && !owner.isEmpty() && !owner.equals("null"))
		{
			this.owner = UUID.fromString(tag.getString("owner"));
		}
	}
}
