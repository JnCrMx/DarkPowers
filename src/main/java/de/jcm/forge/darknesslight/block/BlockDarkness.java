package de.jcm.forge.darknesslight.block;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockDarkness extends Block implements ICraftable
{
	
	public BlockDarkness()
	{
		super(Material.rock);
		setHardness(25.0f);
		setResistance(100.0f);
		setBlockName("darkness");
		setBlockTextureName(DarknessVsLight.MODID + ":" + "darkness");
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "aaa", "aaa", 'a', DarknessVsLight.itemDarkIngot);
	}
	
}
