package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDarkAxe extends ItemAxe implements ICraftable
{
	
	public ItemDarkAxe()
	{
		super(DarkPowers.toolMaterialEngerium);
		setUnlocalizedName("energium_axe");
		setTextureName(DarkPowers.MODID + ":" + "energium_axe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aa#", "ab#", "#b#", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#aa", "#ab", "#b", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
	}
}
