package de.jcm.forge.darknesslight.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.jcm.forge.darknesslight.PlayerData;
import de.jcm.forge.darknesslight.DarkSkills;
import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiSkills extends GuiScreen
{
	private EntityPlayer player;
	private int levelFlash;
	private int levelJump;
	private PlayerData data;
	
	private List<GuiSkillTab> skillTabs;
	private int selectedSkill;
	
	public GuiSkills(EntityPlayer player)
	{
		super();
		this.player=player;
		data=PlayerData.get(player);
		
		this.skillTabs=new ArrayList<GuiSkillTab>();
	}
	
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width/2 - 100, (int) (this.height/2)+70, I18n.format("gui.done", new Object[0])));
		
		if(data.isDarkMage() || data.isDarkness())
		{
			for (DarkSkills skill : DarkSkills.values())
			{
				this.skillTabs.add(new GuiSkillTab(skill.getName()));
			}
		}
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		GL11.glColor3d(1, 1, 1);
		
		this.drawBackground(p_73863_1_);
		this.drawForeground();
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		
		this.drawHover(p_73863_1_, p_73863_2_);
	}
	
	public void drawForeground()
	{
		for (int i=0;i<skillTabs.size();i++)
		{
			GuiSkillTab tab=skillTabs.get(i);
			tab.draw(this.width/2 - 128 + 10 + i*(32+5), this.height/2 - 100 + 10);
		}
		
		if(data.isDarkMage() || data.isDarkness())
			fontRendererObj.drawSplitString(DarkSkills.values()[selectedSkill].getLocalizedDescription(), this.width/2 - 128 + 10, this.height/2 - 100 + 20 + 32, 256-10, Color.BLACK.getRGB());
	}
	
	public void drawHover(int x, int y)
	{
		int minY=this.height/2 - 100 + 10;
		int maxY=minY+32;
		
		if(y>=minY && y<maxY)
		{			
			int element=(x-this.width/2+128-10)/(32+5);
			if(element>=0 && element<skillTabs.size())
			{
				int x1=this.width/2 - 128 + 10 + element*(32+5);
				int x2=x1+32;
				
				if(x>=x1 && x<x2)
				{
					if(data.isDarkMage() || data.isDarkness())
					{
						String skill=DarkSkills.values()[element].getLocalizedName();
						
						drawHoveringText(Arrays.asList(new String[]{skill}), x, y, Minecraft.getMinecraft().fontRenderer);
					}
				}
			}
		}
	}
	
	@Override
	public void drawBackground(int p_146278_1_)
	{		
		ResourceLocation background = new ResourceLocation(DarknessVsLight.MODID.toLowerCase(), "textures/gui/skills.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		
		drawTexturedModalRect(this.width/2 - 256/2, this.height/2 - 200/2, 0, 0, 256, 200);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int flag)
	{
		int minY=this.height/2 - 100 + 10;
		int maxY=minY+32;
		
		if(y>=minY && y<maxY)
		{			
			int element=(x-this.width/2+128-10)/(32+5);
			if(element>=0 && element<skillTabs.size())
			{
				int x1=this.width/2 - 128 + 10 + element*(32+5);
				int x2=x1+32;
				
				if(x>=x1 && x<x2)
				{
					selectedSkill=element;
				}
			}
		}
		
		super.mouseClicked(x, y, flag);
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_)
	{
		if(p_146284_1_.id==0)
		{
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}
}
