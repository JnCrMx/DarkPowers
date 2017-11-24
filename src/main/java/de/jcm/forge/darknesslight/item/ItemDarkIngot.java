package de.jcm.forge.darknesslight.item;

import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.item.Item;

public class ItemDarkIngot extends Item
{
	public ItemDarkIngot()
	{
		setTextureName(DarknessVsLight.MODID + ":" + "darkness_ingot");
		setUnlocalizedName("darkness_ingot");
	}
}
