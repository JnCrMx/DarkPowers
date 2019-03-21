package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDarkHoe extends ItemHoe implements ICraftable
{
	
	public ItemDarkHoe()
	{
		super(DarkPowers.toolMaterialEngerium);
		setUnlocalizedName("energium_hoe");
		setTextureName(DarkPowers.MODID + ":" + "energium_hoe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aa#", "#b#", "#b#", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#aa", "##b", "##b", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
	}
}
