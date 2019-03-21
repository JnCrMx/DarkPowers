package de.jcm.darkpowers.client.render.entity;

import de.jcm.darkpowers.entity.boss.EntityDarkness;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;

public class RenderLivingEntityBossDarkness extends RendererLivingEntity 
{
	public RenderLivingEntityBossDarkness(ModelBase p_i1261_1_, float p_i1261_2_) 
	{
		super(p_i1261_1_, p_i1261_2_);
	}
	
	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) 
	{
		doRender((EntityDarkness)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
	
	public void doRender(EntityDarkness entity, double x, double y, double z, float yaw, float pitch)
	{
		BossStatus.setBossStatus(entity, false);
		
		super.doRender(entity, x, y, z, yaw, pitch);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) 
	{
		return null;
	}

}
