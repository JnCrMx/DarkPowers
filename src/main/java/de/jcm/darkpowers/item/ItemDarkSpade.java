package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDarkSpade extends ItemSpade implements ICraftable
{
	
	public ItemDarkSpade()
	{
		super(DarkPowers.toolMaterialEngerium);
		setUnlocalizedName("energium_shovel");
		setTextureName(DarkPowers.MODID + ":" + "energium_shovel");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a##", "b##", "b##", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#a#", "#b#", "#b#", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "##a", "##b", "##b", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
	}
	
}
