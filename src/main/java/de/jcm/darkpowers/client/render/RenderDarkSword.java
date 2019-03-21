package de.jcm.darkpowers.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.ItemStack;

import net.minecraftforge.client.IItemRenderer;

public class RenderDarkSword implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) 
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		try
		{
			GL11.glPushMatrix();
			
			//TODO: Third person, inventory
			GL11.glTranslated(1, 0, 0);
			GL11.glScaled(0.7, 0.7, 0.7);
			/*
			IModelCustom model=AdvancedModelLoader.loadModel(new ResourceLocation(DarkPowers.MODID, "obj/dark_sword.obj"));
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(DarkPowers.MODID, "textures/gui/red.png"));
			GL11.glColor3d(0.0, 0.0, 0.0);
			model.renderAll();
			*/
			GL11.glPopMatrix();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
