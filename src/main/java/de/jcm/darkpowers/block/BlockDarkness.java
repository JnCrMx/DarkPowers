package de.jcm.darkpowers.block;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;
import de.jcm.darkpowers.item.DarkItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockDarkness extends Block implements ICraftable
{

	public BlockDarkness()
	{
		super(Material.rock);
		setHardness(25.0f);
		setResistance(100.0f);
		setBlockName("darkness");
		setBlockTextureName(DarkPowers.MODID + ":" + "darkness");
	}

	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "aaa", "aaa", 'a', DarkItems.itemDarkIngot);
	}

}
