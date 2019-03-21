package de.jcm.darkpowers.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityDarkness extends EntityMob implements IBossDisplayData, IRangedAttackMob 
{
	private int phase;

	public EntityDarkness(World p_i1738_1_) 
	{
		super(p_i1738_1_);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) 
	{
		
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000000.0D);
	}
	
	@Override
	protected void entityInit() 
	{
		super.entityInit();
	}
}
