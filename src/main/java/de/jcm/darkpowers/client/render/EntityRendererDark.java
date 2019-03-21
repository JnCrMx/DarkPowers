package de.jcm.darkpowers.client.render;

import java.lang.reflect.Field;

import de.jcm.darkpowers.ClientProxy;
import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityRendererDark extends EntityRenderer
{
	private final int[] lightmapColors;
	private final DynamicTexture lightmapTexture;
	private final Field f_lightmapUpdateNeeded;
	
	public EntityRendererDark(Minecraft p_i45076_1_, IResourceManager p_i45076_2_)
	{
		super(p_i45076_1_, p_i45076_2_);
		
		int[] lightmapColors = null;
		DynamicTexture lightmapTexture = null;
		Field f_lightmapUpdateNeeded = null;
		try
		{			
			Field f_lightmapColors = ReflectionHelper.findField(EntityRenderer.class, "lightmapColors", "field_78504_Q");
			f_lightmapColors.setAccessible(true);
			lightmapColors = (int[]) f_lightmapColors.get(this);
			
			Field f_lightmapTexture = ReflectionHelper.findField(EntityRenderer.class, "lightmapTexture", "field_78513_d");
			f_lightmapTexture.setAccessible(true);
			lightmapTexture = (DynamicTexture) f_lightmapTexture.get(this);
			
			f_lightmapUpdateNeeded = ReflectionHelper.findField(EntityRenderer.class, "lightmapUpdateNeeded", "field_78536_aa");
			f_lightmapUpdateNeeded.setAccessible(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.lightmapColors = lightmapColors;
		this.lightmapTexture = lightmapTexture;
		this.f_lightmapUpdateNeeded = f_lightmapUpdateNeeded;
	}
	
	@Override
	public void renderWorld(float p_78471_1_, long p_78471_2_)
	{
		try
		{
			if(f_lightmapUpdateNeeded.getBoolean(this))
			{
				updateLightmap();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		super.renderWorld(p_78471_1_, p_78471_2_);
	}
	
	private void updateLightmap()
	{
		ClientProxy proxy = (ClientProxy) DarkPowers.commonProxy;
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		WorldClient world = Minecraft.getMinecraft().theWorld;
		PlayerData data = PlayerData.get(player);
		
		boolean renderDark = false;
		if(!renderDark && proxy.renderDark)
		{
			renderDark = true;
		}
		if(!renderDark && proxy.renderDome!=null)
		{
			if(world.getBlock(proxy.renderDome.xCoord, proxy.renderDome.yCoord, proxy.renderDome.zCoord)==DarkPowers.blockDarkDome)
			{
				if(TileEntityDarkDome.checkInDome(proxy.renderDome, player))
				{
					renderDark = true;
				}
			}
		}
		
		if(data.getEnabledPassiveSkills().contains(DarkSkills.EYE_DARKNESS))
		{
			for(int i=0;i<256;i++)
			{
				//TODO: Check
				lightmapColors[i]=255 << 24 | 255 << 16 | 255 << 8 | 255;	//not the best option, but it works
			}
			lightmapTexture.updateDynamicTexture();
			
			try
			{
				f_lightmapUpdateNeeded.setBoolean(this, false);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(renderDark)	//Eye of Darkness prevents rendering darkness
		{
			for(int i=0;i<256;i++)
			{
				//TODO: Check
				lightmapColors[i]=255 << 24 | 16 << 16 | 16 << 8 | 16;
			}
			lightmapTexture.updateDynamicTexture();
			
			try
			{
				f_lightmapUpdateNeeded.setBoolean(this, false);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateCameraAndRender(float p_78480_1_)
	{
		try
		{
			if(f_lightmapUpdateNeeded.getBoolean(this))
			{
				updateLightmap();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		super.updateCameraAndRender(p_78480_1_);
	}
}
