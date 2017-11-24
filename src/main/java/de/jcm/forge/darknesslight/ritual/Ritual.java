package de.jcm.forge.darknesslight.ritual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class Ritual extends Thread
{
	protected EntityPlayer player;
	protected World world;
	protected int x, y, z;
	
	public Ritual(EntityPlayer player, int x, int y, int z)
	{
		this.player = player;
		this.world = player.worldObj;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public abstract void onCancel() throws Exception;
	
	public abstract boolean isCompleted();
}
