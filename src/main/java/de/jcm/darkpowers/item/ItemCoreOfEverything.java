package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ICraftable;

import net.minecraft.item.Item;

public class ItemCoreOfEverything extends Item implements ICraftable
{
	
	public ItemCoreOfEverything()
	{
		setTextureName(DarkPowers.MODID + ":" + "core_of_everything");
		setUnlocalizedName("core_of_everything");
	}
	
	@Override
	public void registerRecipe()
	{
		
	}
	
}
