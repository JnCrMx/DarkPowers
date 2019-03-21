package de.jcm.darkpowers.block;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockDarkDome extends BlockContainer
{
	public BlockDarkDome()
	{
		super(Material.glass);
		setBlockUnbreakable();
		setBlockName("dark_dome");
		setBlockTextureName(DarkPowers.MODID + ":" + "dark_dome");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	{
		return null;
	}
	
	@Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public int getRenderType() 
    {
    	return -1;
    }
    
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) 
	{
    	return new TileEntityDarkDome();
	}
}
