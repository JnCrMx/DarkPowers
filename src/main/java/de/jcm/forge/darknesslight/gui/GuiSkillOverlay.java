package de.jcm.forge.darknesslight.gui;

import java.awt.Color;

import de.jcm.forge.darknesslight.ClientProxy;
import de.jcm.forge.darknesslight.Constants;
import de.jcm.forge.darknesslight.DarkSkills;
import de.jcm.forge.darknesslight.PlayerData;
import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

//TODO: Add light side
public class GuiSkillOverlay extends Gui
{
	private EntityPlayer player;
	
	private int ticksFlash;
	private int ticksJump;
	
	private int levelFlash;
	private int levelJump;
	
	private int oldTickCheck;

	private int failCount;

	private boolean dark;
	
	public GuiSkillOverlay(EntityPlayer player)
	{
		this.player=player;
	}
	
	public void render()
	{
		try
		{
			// --------------------
			float flashF = ticksFlash;
			float flashS = flashF / 20;
			float flashP = flashF / (Constants.FLASH_COOLDOWN);
			
			float jumpF = ticksJump;
			float jumpS = jumpF / 20;
			float jumpP = jumpF / (Constants.JUMP_COOLDOWN);
			
			// --------------------
			ResourceLocation flashRes = new ResourceLocation(DarknessVsLight.MODID.toLowerCase(), "textures/gui/skill_flash.png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(flashRes);
			
			//if(levelFlash>0)
				drawTexturedModalRect(0, 0, 0, 0, 32, 32);
			
			ResourceLocation jumpRes = new ResourceLocation(DarknessVsLight.MODID.toLowerCase(), "textures/gui/skill_jump.png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(jumpRes);
			
			//if(levelJump>0)
				drawTexturedModalRect(0, 52, 0, 0, 32, 32);
			
			// --------------------
			//if(levelFlash>0)
				drawString(Minecraft.getMinecraft().fontRenderer, Float.toString(round(flashS, 1)) + "s", 0, 32, Color.RED.getRGB());
			//if(levelJump>0)
				drawString(Minecraft.getMinecraft().fontRenderer, Float.toString(round(jumpS, 1)) + "s", 0, 52 + 32, Color.RED.getRGB());
			
			// --------------------
			//if(levelFlash>0)
				Minecraft.getMinecraft().fontRenderer.drawString(Integer.toString(levelFlash), 32-10, 32-12, dark?Color.WHITE.getRGB():Color.BLACK.getRGB(), false);
			//if(levelJump>0)
				Minecraft.getMinecraft().fontRenderer.drawString(Integer.toString(levelJump), 32-10, 52+32-12, dark?Color.WHITE.getRGB():Color.BLACK.getRGB(), false);
			
			// --------------------
			int flashPx = (int) (flashP * 32F);
			int jumpPx = (int) (jumpP * 32F);
			
			//if(levelFlash>0)
				drawRect(0, 0, 32, flashPx, new Color(1f, 1f, 1f, 0.5f).getRGB());
			//if(levelJump>0)
				drawRect(0, 52, 32, jumpPx + 52, new Color(1f, 1f, 1f, 0.5f).getRGB());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void tick(int ticksFlash, int ticksJump)
	{		
		this.ticksFlash = ticksFlash;
		this.ticksJump = ticksJump;
	}
	
	public void tick()
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
		
		PlayerData data=PlayerData.get(player);
		if(data!=null)
		{			
			this.levelFlash=data.getLevel(DarkSkills.FLASH);
			this.levelJump=data.getLevel(DarkSkills.JUMP);

			this.ticksFlash=data.getCooldown(DarkSkills.FLASH);
			this.ticksJump=data.getCooldown(DarkSkills.JUMP);
			
			this.dark=data.isDarkness() || data.isDarkMage();
		}
	}
	
	private static float round(float f, int st)
	{
		float exp2 = up10(st);
		float f2 = f * exp2;
		
		float f3 = Math.round(f2);
		
		return f3 / exp2;
	}
	
	private static int up10(int exp)
	{
		int var = 1;
		for (int i = 0; i < exp; i++)
		{
			var = var * 10;
		}
		
		return var;
	}
}
