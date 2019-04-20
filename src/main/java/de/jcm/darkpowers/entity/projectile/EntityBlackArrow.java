package de.jcm.darkpowers.entity.projectile;

import java.util.List;
import java.util.UUID;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.network.PacketClientEffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityBlackArrow extends EntityFlying
{
	public EntityBlackArrow(World world)
	{
		super(world);
        this.setSize(1F, 1F);
	}

	public EntityBlackArrow(World world, EntityPlayer owner, double targetX, double targetY, double targetZ)
	{
		super(world);
        this.setSize(1F, 1F);
        this.setPositionAndUpdate(owner.posX, owner.posY, owner.posZ);

        setTarget(Vec3.createVectorHelper(targetX, targetY, targetZ));
        setOwnerUUID(owner.getUniqueID());
	}

	@Override
	protected void collideWithEntity(Entity entity)
	{
		super.collideWithEntity(entity);
	}

	@Override
	public void onUpdate()
	{
        ticksExisted++;

        Vec3 pos = Vec3.createVectorHelper(posX, posY, posZ);
        Vec3 target = getTarget();

        Vec3 maxMovement = pos.subtract(target);
        Vec3 direction = maxMovement.normalize();

        double length = Math.min(1, maxMovement.lengthVector());
        Vec3 movement = Vec3.createVectorHelper(direction.xCoord*length, direction.yCoord*length, direction.zCoord*length);

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
        	this.boundingBox.addCoord(movement.xCoord, movement.yCoord, movement.zCoord).expand(2.5D, 2.5D, 2.5D));
        for(Object object : list)
		{
			if(object instanceof EntityLivingBase)
			{
				EntityLivingBase entity = (EntityLivingBase)object;
				if(!entity.getUniqueID().equals(getOwnerUUID()))
				{
		    		if(getOwner()!=null)
		    		{
		    			entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
		    		}
		    		else
		    		{
		    			entity.attackEntityFrom(DamageSource.magic, 10.0f);
		    		}
				}
			}
		}

        getLookHelper().setLookPosition(target.xCoord, target.yCoord, target.zCoord, Float.MAX_VALUE, Float.MAX_VALUE);
        getLookHelper().onUpdateLook();
        rotationYaw=rotationYawHead;

        posX+=movement.xCoord;
        posY+=movement.yCoord;
        posZ+=movement.zCoord;

        if(ticksExisted>100)
        {
        	setDead();
        }
        if(movement.xCoord==0 && movement.yCoord==0 && movement.zCoord==0)
        {
        	setDead();
        }
	}

	@Override
	public void setDead()
	{
    	if(getOwner()!=null)
    	{
        	getOwner().setPositionAndUpdate(posX, posY, posZ);
        	getOwner().setInvisible(false);
        	DarkPowers.wrapper.sendToAllAround(new PacketClientEffect(getOwner(), ClientEffect.BLACK_ARROW, -1),
        		new TargetPoint(dimension, posX, posY, posZ, 100));
    	}
    	if(!worldObj.isRemote)
    	{
    		DarkPowers.commonProxy.blackArrowKills.remove(this.getEntityId());
    	}
		super.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);

		tag.setString("OwnerUUID", getOwnerUUID().toString());

		Vec3 target = getTarget();
		tag.setFloat("TargetX", (float) target.xCoord);
		tag.setFloat("TargetY", (float) target.yCoord);
		tag.setFloat("TargetZ", (float) target.zCoord);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);

		setOwnerUUID(UUID.fromString(tag.getString("Owner")));

		double x = tag.getFloat("TargetX");
		double y = tag.getFloat("TargetY");
		double z = tag.getFloat("TargetZ");
		setTarget(Vec3.createVectorHelper(x, y, z));
	}

	public UUID getOwnerUUID()
	{
		return UUID.fromString(this.dataWatcher.getWatchableObjectString(17));
	}

	public void setOwnerUUID(UUID owner)
	{
		this.dataWatcher.updateObject(17, owner.toString());
	}

	public EntityPlayer getOwner()
	{
		return this.worldObj.func_152378_a(getOwnerUUID());
	}

	public void setTarget(Vec3 target)
	{
		this.dataWatcher.updateObject(18, (float) target.xCoord);
		this.dataWatcher.updateObject(19, (float) target.yCoord);
		this.dataWatcher.updateObject(20, (float) target.zCoord);
	}

	public Vec3 getTarget()
	{
		float x = this.dataWatcher.getWatchableObjectFloat(18);
		float y = this.dataWatcher.getWatchableObjectFloat(19);
		float z = this.dataWatcher.getWatchableObjectFloat(20);

		return Vec3.createVectorHelper(x, y, z);
	}

	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(17, "");
        this.dataWatcher.addObject(18, 0.0f);
        this.dataWatcher.addObject(19, 0.0f);
        this.dataWatcher.addObject(20, 0.0f);
    }
}
