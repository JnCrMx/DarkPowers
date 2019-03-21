package de.jcm.darkpowers.gui;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.util.GUIHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class GuiSkillTab extends Gui
{
	private DarkSkills skill;
	private PlayerData data;
	
	public GuiSkillTab(DarkSkills skill, PlayerData data)
	{
		this.skill=skill;
		this.data=data;
	}
	
	public void draw(int x, int y)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(skill.getTextureResource());
		drawTexturedRect(x+6, y+6, 0, 0, 25, 25, 25, 25, !data.getUnlocks().contains(skill));
		
		ResourceLocation border = new ResourceLocation(DarkPowers.MODID.toLowerCase(), 
			"textures/gui/skill_"+(skill.isSpecial()?"special":"normal")+"_border.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(border);
		GUIHelper.drawTexturedRect(x, y, 0, 0, 38, 38, 38, 38);
	}
	
	public void drawTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, boolean locked)
    {
        float f = 1F / (float)textureWidth;
        float f1 = 1F / (float)textureHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        
        if(locked)
        {
        	tessellator.setColorRGBA(50, 50, 50, 255);
        }
        
        tessellator.addVertexWithUV((double)(x), (double)(y + height), 0, (double)((float)(u) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0, (double)((float)(u + width) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y), 0, (double)((float)(u + width) * f), (double)((float)(v) * f1));
        tessellator.addVertexWithUV((double)(x), (double)(y), 0, (double)((float)(u) * f), (double)((float)(v) * f1));
        tessellator.draw();
    }

	public DarkSkills getSkill()
	{
		return skill;
	}
}
