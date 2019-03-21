package de.jcm.darkpowers.world.dimension;

import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Point3d;

import de.jcm.darkpowers.DarkPowers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import net.minecraftforge.common.util.Constants;

public class PortalWorldData extends WorldSavedData implements Iterable<Point3d>
{
	public static final String key = "de.jcm.world.data.portal";
	
	private ArrayList<Point3d> portalsInOverworld;
	
	public PortalWorldData()
	{
		super(key);
		portalsInOverworld = new ArrayList<Point3d>();
	}
	
	public PortalWorldData(String p_i2141_1_)
	{
		super(p_i2141_1_);
		portalsInOverworld = new ArrayList<Point3d>();
	}
	
	public boolean contains(Object o)
	{
		return portalsInOverworld.contains(o);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_)
	{
		DarkPowers.logger.debug("read");
		portalsInOverworld.clear();
		
		NBTTagList list = p_76184_1_.getTagList("portals", Constants.NBT.TAG_COMPOUND);
		
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound entry = list.getCompoundTagAt(i);
			
			double x = entry.getDouble("x");
			double y = entry.getDouble("y");
			double z = entry.getDouble("z");
			
			Point3d point = new Point3d(x, y, z);
			portalsInOverworld.add(point);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_)
	{
		DarkPowers.logger.debug("write");
		
		NBTTagList list = new NBTTagList();
		
		for (Point3d point3d : portalsInOverworld)
		{
			NBTTagCompound entry = new NBTTagCompound();
			
			entry.setDouble("x", point3d.x);
			entry.setDouble("y", point3d.y);
			entry.setDouble("z", point3d.z);
			
			list.appendTag(entry);
		}
		
		p_76187_1_.setTag("portals", list);
	}
	
	public static PortalWorldData forWorld(World world)
	{
		PortalWorldData result = (PortalWorldData) world.loadItemData(PortalWorldData.class, key);
		if (result == null)
		{
			result = new PortalWorldData();
			world.setItemData(key, result);
		}
		return result;
	}
	
	public ArrayList<Point3d> getPortalsInOverworld()
	{
		return portalsInOverworld;
	}
	
	public boolean add(Point3d arg0)
	{
		return portalsInOverworld.add(arg0);
	}
	
	public Point3d get(int arg0)
	{
		return portalsInOverworld.get(arg0);
	}
	
	public boolean remove(Object arg0)
	{
		return portalsInOverworld.remove(arg0);
	}
	
	@Override
	public Iterator<Point3d> iterator()
	{
		return portalsInOverworld.iterator();
	}
	
}
