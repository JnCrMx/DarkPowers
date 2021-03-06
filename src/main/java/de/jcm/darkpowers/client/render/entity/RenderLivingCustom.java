package de.jcm.darkpowers.client.render.entity;

import de.jcm.darkpowers.DarkPowers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderLivingCustom extends RenderLiving
{
	
	protected ResourceLocation texture;
	
	public RenderLivingCustom(ModelBase par1ModelBase, float parShadowSize, String entityTextureName)
	{
		super(par1ModelBase, parShadowSize);
		setEntityTexture(entityTextureName);
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		
		// some people do some G11 transformations or blends here, like you can
		// do
		
		// GL11.glScalef(2F, 2F, 2F); to scale up the entity
		
		// which is used for Slime entities. I suggest having the entity cast to
		
		// your custom type to make it easier to access fields from your
		
		// custom entity, eg. GL11.glScalef(entity.scaleFactor,
		// entity.scaleFactor,
		
		// entity.scaleFactor);
		
	}
	
	protected void setEntityTexture(String entityTextureName)
	{
		texture = new ResourceLocation(DarkPowers.MODID + ":" + "textures/entity/" + entityTextureName + ".png");
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return texture;
		
	}
}
