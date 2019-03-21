package de.jcm.darkpowers.world.dimension;

import java.util.ArrayList;

import javax.vecmath.Point3d;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterDarkness extends Teleporter
{
	
	public TeleporterDarkness(WorldServer p_i1963_1_)
	{
		super(p_i1963_1_);
	}
	
	@Override
	public boolean makePortal(Entity entity)
	{
		return false;
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float f)
	{
		if (entity.dimension == 2)
		{
			entity.setPosition(8.5, 41.5, 8.5);
			return true;
		}
		if (entity.dimension == 0)
		{
			// Point3d is just a data holding class here
			Point3d pos1 = new Point3d(entity.posX, entity.posY, entity.posZ);
			
			double minDistance = Double.MAX_VALUE;
			Point3d portal = new Point3d();
			
			ArrayList<Point3d> portalsInOverworld = PortalWorldData.forWorld(entity.worldObj).getPortalsInOverworld();
			
			for (Point3d point3d : portalsInOverworld)
			{
				double distance = pos1.distance(point3d);
				if (distance < minDistance)
				{
					portal = point3d;
					minDistance = distance;
				}
			}
			
			entity.setPosition(portal.x + 0.5, portal.y + 1, portal.z + 0.5);
			
			return true;
		}
		return false;
	}
}
