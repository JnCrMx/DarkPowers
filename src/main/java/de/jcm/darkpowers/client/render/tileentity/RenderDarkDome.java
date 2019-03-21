package de.jcm.darkpowers.client.render.tileentity;

import org.lwjgl.opengl.GL11;

import de.jcm.darkpowers.tileentity.TileEntityDarkDome;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderDarkDome extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
	{
		renderTileEntityAt((TileEntityDarkDome)tileEntity, x, y, z, f);
	}
	
	public void renderTileEntityAt(TileEntityDarkDome tileEntity, double x, double y, double z, float f)
	{	
		GL11.glPushMatrix();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d(0, 0, 0, 0.9);
		
		GL11.glTranslated(x+0.5, y, z+0.5);
		
		double r = tileEntity.radius;
		double stepY = (Math.PI/2)/3;
		double stepXZ = (Math.PI*2)/8;
		
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glBegin(GL11.GL_QUADS);
		for(double i=0;i<Math.PI*0.5;i+=stepY)
		{
			for(double j=0;j<Math.PI*2;j+=stepXZ)
			{
				double r1 = Math.sin(i)*r;
				double r2 = Math.sin(i+stepY)*r;
				GL11.glVertex3d(Math.sin(j)*r1, Math.cos(i)*r*2-r*2, Math.cos(j)*r1);
				GL11.glVertex3d(Math.sin(j+stepXZ)*r1, Math.cos(i)*r*2-r*2, Math.cos(j+stepXZ)*r1);
				GL11.glVertex3d(Math.sin(j+stepXZ)*r2, Math.cos(i+stepY)*r*2-r*2, Math.cos(j+stepXZ)*r2);
				GL11.glVertex3d(Math.sin(j)*r2, Math.cos(i+stepY)*r*2-r*2, Math.cos(j)*r2);
			}
		}
		GL11.glEnd();
		
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glBegin(GL11.GL_QUADS);
		for(double i=0;i<Math.PI*0.5;i+=stepY)
		{
			for(double j=0;j<Math.PI*2;j+=stepXZ)
			{
				double r1 = Math.sin(i)*r;
				double r2 = Math.sin(i+stepY)*r;
				GL11.glVertex3d(Math.sin(j)*r1, Math.cos(i)*r*2-r*2, Math.cos(j)*r1);
				GL11.glVertex3d(Math.sin(j+stepXZ)*r1, Math.cos(i)*r*2-r*2, Math.cos(j+stepXZ)*r1);
				GL11.glVertex3d(Math.sin(j+stepXZ)*r2, Math.cos(i+stepY)*r*2-r*2, Math.cos(j+stepXZ)*r2);
				GL11.glVertex3d(Math.sin(j)*r2, Math.cos(i+stepY)*r*2-r*2, Math.cos(j)*r2);
			}
		}
		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPopMatrix();
	}
}
