package de.jcm.darkpowers.ritual;

import java.util.function.Supplier;

import de.jcm.darkpowers.tileentity.TileEntityAltar;

import net.minecraft.entity.player.EntityPlayer;

public class RitualCollision extends Ritual
{
	public RitualCollision(Supplier<EntityPlayer> player, TileEntityAltar altar, int x, int y, int z)
	{
		super(player, altar, x, y, z);
	}

	@Override
	public boolean checkPreconditions()
	{
		return false;
	}
	
	@Override
	public double getRadius()
	{
		return 10;
	}
	
}
