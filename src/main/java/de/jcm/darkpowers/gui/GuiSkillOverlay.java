package de.jcm.darkpowers.gui;

import java.awt.Color;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.util.GUIHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSkillOverlay extends Gui
{
	public EntityPlayer player;
	
	public GuiSkillOverlay(EntityPlayer player)
	{
		this.player=player;
	}
	
	public void render()
	{		
		PlayerData data=PlayerData.get(player);
		DarkSkills[] skills = data.getSelectedSkills();
		
		for(int i=0;i<skills.length;i++)
		{
			DarkSkills skill = skills[i];
			if(skill!=null)
			{
				float cooldownTicks = data.getCooldown(skill);
				float cooldownSeconds = cooldownTicks / 20;
				float cooldownPercent = cooldownTicks / skill.getCooldown();
				int cooldownPixels = (int) (cooldownPercent * 25F);
				
				Minecraft.getMinecraft().getTextureManager().bindTexture(skill.getTextureResource());
				GUIHelper.drawTexturedRect(1+6, i*40+1+6, 0, 0, 25, 25, 25, 25);
				ResourceLocation border = new ResourceLocation(DarkPowers.MODID.toLowerCase(), 
					"textures/gui/skill_"+(skill.isSpecial()?"special":"normal")+"_border.png");
				Minecraft.getMinecraft().getTextureManager().bindTexture(border);
				GUIHelper.drawTexturedRect(1, i*40+1, 0, 0, 38, 38, 38, 38);
				
				drawString(Minecraft.getMinecraft().fontRenderer, Float.toString(round(cooldownSeconds, 1)) + "s", 1+6, i*40+1+6, Color.RED.getRGB());
				
				drawRect(1+6, i*40+1+6, 25+1+6, i*40+cooldownPixels+1+6, new Color(1f, 1f, 1f, 0.5f).getRGB());
			}
		}
		
		if(data.getRemainingBarrierHits()>0)
		{
			ResourceLocation shield = new ResourceLocation(DarkPowers.MODID.toLowerCase(), "textures/gui/shield.png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(shield);

			int width = Minecraft.getMinecraft().displayWidth;
			int height = Minecraft.getMinecraft().displayHeight;
			
			int x = width/4-120;
			int y = height/2-28;
			
			GUIHelper.drawTexturedRect(x, y, 0, 0, 28, 28, 28, 28);
			Minecraft.getMinecraft().fontRenderer.drawString(
				Integer.toString(data.getRemainingBarrierHits()), x+12, y+9, Color.WHITE.getRGB());
		}
	}
	
	private static float round(float f, int st)
	{
		float exp2 = (float) Math.pow(10, st);
		float f2 = f * exp2;
		
		float f3 = Math.round(f2);
		
		return f3 / exp2;
	}
}
