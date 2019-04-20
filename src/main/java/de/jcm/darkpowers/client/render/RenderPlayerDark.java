package de.jcm.darkpowers.client.render;

import org.lwjgl.opengl.GL11;

import de.jcm.darkpowers.ClientProxy;
import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.client.ClientEffect;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;

public class RenderPlayerDark extends RenderPlayerBase
{
	public RenderPlayerDark(RenderPlayerAPI arg0)
	{
		super(arg0);
	}

	public void renderDarkBarrier(int strength)
	{
		GL11.glPushMatrix();

		GL11.glTranslated(0, 0.5, 0);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glCullFace(GL11.GL_FRONT);

		double alpha = (strength+10)*0.05;
		if(alpha>1)
			alpha=1;

		GL11.glColor4d(0, 0, 0, alpha);

		GL11.glBegin(GL11.GL_QUADS);

		double r = 1.25;
		double step = Math.PI/20;
		for(double i=0;i<Math.PI;i+=step)
		{
			for(double j=0;j<Math.PI*2;j+=step)
			{
				double r1 = Math.sin(i)*r;
				double r2 = Math.sin(i+step)*r;
				GL11.glVertex3d(Math.sin(j)*r1, Math.cos(i)*r, Math.cos(j)*r1);
				GL11.glVertex3d(Math.sin(j+step)*r1, Math.cos(i)*r, Math.cos(j+step)*r1);
				GL11.glVertex3d(Math.sin(j+step)*r2, Math.cos(i+step)*r, Math.cos(j+step)*r2);
				GL11.glVertex3d(Math.sin(j)*r2, Math.cos(i+step)*r, Math.cos(j)*r2);
			}
		}

		GL11.glEnd();

		GL11.glCullFace(GL11.GL_BACK);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPopMatrix();
	}

	@Override
	public void renderPlayer(AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		PlayerData data = PlayerData.get(paramAbstractClientPlayer);
		if(data.getClientEffect()==ClientEffect.BLACK_ARROW)
		{
			return;
		}
		super.renderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	@Override
	public void renderSpecials(AbstractClientPlayer arg0, float arg1)
	{
		super.renderSpecials(arg0, arg1);

		ClientProxy proxy = (ClientProxy) DarkPowers.commonProxy;

		PlayerData data = (PlayerData) arg0.getExtendedProperties(PlayerData.IDENTIFIER);
		if(data.getRemainingBarrierHits()>0)
		{
			renderDarkBarrier(data.getRemainingBarrierHits());
		}
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer paramEntityPlayer)
	{
		super.renderFirstPersonArm(paramEntityPlayer);

	}
}
