package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDarkSword extends ItemSword implements ICraftable
{
	
	public ItemDarkSword()
	{
		super(DarkPowers.toolMaterialDark);
		setUnlocalizedName("dark_sword");
		setTextureName(DarkPowers.MODID + ":" + "dark_sword");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a##", "a##", "b##", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#a#", "#a#", "#b#", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "##a", "##a", "##b", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
	}
}
