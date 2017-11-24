package de.jcm.forge.darknesslight.block;

import de.jcm.forge.darknesslight.ICraftable;
import de.jcm.forge.darknesslight.ritual.RitualConnection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockRitualTable extends Block implements ICraftable
{
	public BlockRitualTable()
	{
		super(Material.rock);
		setHardness(25.0f);
		setResistance(100.0f);
		setBlockName("ritual_table");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3)
	{
		if (!world.isRemote)
		{
			RitualConnection connection = new RitualConnection(player, x, y, z);
			if (connection.isCompleted())
			{
				connection.start();
			}
		}
		return true;
	}
	
	@Override
	public void registerRecipe()
	{
		// TODO: Add
	}
}
