package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDarkPickaxe extends ItemPickaxe implements ICraftable
{
	
	public ItemDarkPickaxe()
	{
		super(DarkPowers.toolMaterialEngerium);
		setUnlocalizedName("energium_pickaxe");
		setTextureName(DarkPowers.MODID + ":" + "energium_pickaxe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "#b#", "#b#", 'a', DarkPowers.itemDarkIngot, 'b', Items.stick);
	}
}
