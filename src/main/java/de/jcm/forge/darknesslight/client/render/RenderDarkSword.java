package de.jcm.forge.darknesslight.client.render;

import org.lwjgl.opengl.GL11;

import de.jcm.forge.darknesslight.client.model.ModelDarkSword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
			
			boolean var4 = false;
			float var5;
			if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) || RenderManager.instance.playerViewY != 180.0F))
			{
				var4 = true;
				var5 = 0.7F;
				GL11.glTranslatef(1.0F, 0.7F, 0.6F);
				GL11.glScalef(var5, var5, var5);
				GL11.glRotatef(205.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(100.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-5.0F, 1.0F, 0.0F, 0.0F);
			}
			else
			{
				var5 = 0.3F;
				GL11.glTranslatef(0.5F, 0.5F, 0.1F);
				GL11.glScalef(var5, var5, var5);
				GL11.glRotatef(190.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(100.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-5.0F, 1.0F, 0.0F, 0.0F);
			}
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/beacon_beam.png"));
			new ModelDarkSword().render(Minecraft.getMinecraft().renderViewEntity, 100, 100, 1000, 100, 100, 1);
			
			GL11.glPopMatrix();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
