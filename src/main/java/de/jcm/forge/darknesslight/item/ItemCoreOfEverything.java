package de.jcm.forge.darknesslight.item;

import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.item.Item;

public class ItemCoreOfEverything extends Item implements ICraftable
{
	
	public ItemCoreOfEverything()
	{
		setTextureName(DarknessVsLight.MODID + ":" + "core_of_everything");
		setUnlocalizedName("core_of_everything");
	}
	
	@Override
	public void registerRecipe()
	{
		
	}
	
}
