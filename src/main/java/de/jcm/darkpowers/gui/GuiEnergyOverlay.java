package de.jcm.darkpowers.gui;

import java.awt.Color;

import de.jcm.darkpowers.PlayerData;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class GuiEnergyOverlay extends Gui
{
	private EntityPlayer player;
	private int oldTickCheck;
	private int failCount;
	
	public GuiEnergyOverlay(EntityPlayer player)
	{
		this.player = player;
	}
	
	public void render()
	{
		if (player.isDead || failCount > 25)
		{
			player = Minecraft.getMinecraft().thePlayer;
			failCount=0;
		}
		if (player.ticksExisted == oldTickCheck)
		{
			if(Minecraft.getMinecraft().inGameHasFocus)
				failCount++;
		}
		else
			failCount = 0;
		this.oldTickCheck = player.ticksExisted;
		
		PlayerData data = PlayerData.get(player);
		
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
		
		float f1 = data.getEnergy();
		float f2 = data.getMaxEnergy();
		
		float f3 = (f1 / f2) * 90f;
		
		int value = (int) f3;
		
		int y=l-50;
		if(Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.water))
		{
			y=y-10;
		}
		
		drawRect(k / 2 + 10, y, k / 2 + 90, y+10, Color.RED.getRGB());
		drawRect(k / 2 + 90 - value, y, k / 2 + 90, y+10, Color.GREEN.getRGB());
	}
}
