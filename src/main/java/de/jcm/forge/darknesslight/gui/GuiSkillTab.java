package de.jcm.forge.darknesslight.gui;

import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiSkillTab extends Gui
{
	private String skill;
	
	public GuiSkillTab(String skill)
	{
		this.skill=skill;
	}
	
	public void draw(int x, int y)
	{
		ResourceLocation flashRes = new ResourceLocation(DarknessVsLight.MODID.toLowerCase(), "textures/gui/skill_"+skill+".png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(flashRes);
		drawTexturedModalRect(x, y, 0, 0, 32, 32);
	}

	public String getSkill()
	{
		return skill;
	}
}
