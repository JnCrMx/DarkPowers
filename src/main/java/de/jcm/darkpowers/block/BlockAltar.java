package de.jcm.darkpowers.block;

import java.util.Set;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;
import de.jcm.darkpowers.ritual.Ritual;
import de.jcm.darkpowers.tileentity.TileEntityAltar;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAltar extends BlockContainer implements ICraftable
{
	public BlockAltar()
	{
		super(Material.rock);
		setHardness(25.0f);
		setResistance(100.0f);
		setBlockName("altar");
		setBlockTextureName(DarkPowers.MODID + ":" + "altar");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3)
	{
		if (!world.isRemote)
		{
			TileEntityAltar te = (TileEntityAltar) world.getTileEntity(x, y, z);
			if(te.activeRitual==null)
			{
				te.activePlayer=player.getUniqueID();
				Set<String> rituals = Ritual.getNames();
				for(String string : rituals)
				{
					Ritual test = Ritual.createRitual(string, te.player, te, x, y, z);
					if(test.checkPreconditions())
					{
						te.activeRitual=test;
						te.activeRitual.start();
						te.markDirty();
						break;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void registerRecipe()
	{
		// TODO: Add
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityAltar();
	}
}
