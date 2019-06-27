package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;

import cpw.mods.fml.common.registry.GameRegistry;

public class DarkItems
{
	// Items
	public static ItemDarkIngot itemDarkIngot;
	public static ItemBookOfDarkMyths itemBookOfDarkMyths;

	public static void init()
	{
		// Init items
		itemDarkIngot = new ItemDarkIngot();
		itemBookOfDarkMyths = new ItemBookOfDarkMyths();

		// Set items' tabs
		itemDarkIngot.setCreativeTab(DarkPowers.creativeTabDarkness);
		itemBookOfDarkMyths.setCreativeTab(DarkPowers.creativeTabDarkness);

		// Register items
		GameRegistry.registerItem(itemDarkIngot, "darkness_ingot");
		GameRegistry.registerItem(itemBookOfDarkMyths, "book_of_dark_myths");
	}
}
