package de.jcm.darkpowers.inventory;

import java.util.List;

import de.jcm.darkpowers.block.DarkBlocks;
import de.jcm.darkpowers.item.DarkItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabDarkPowersDarkness extends CreativeTabs
{

	public CreativeTabDarkPowersDarkness()
	{
		super("DarkPowersDarkness");
	}

	@Override
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(DarkBlocks.blockDarkness);
	}

	// Force item sorting
	// Standard sorting still not working, used again
	@Override
	public void displayAllReleventItems(List list)
	{
		list.add(new ItemStack(DarkItems.itemDarkIngot, 1));
		list.add(new ItemStack(DarkBlocks.blockDarkness, 1));

		list.add(new ItemStack(DarkBlocks.blockRuneEmpty, 1));
		list.add(new ItemStack(DarkBlocks.blockRuneBinding, 1));
		list.add(new ItemStack(DarkBlocks.blockRuneOpening, 1));
		list.add(new ItemStack(DarkBlocks.blockRuneHolding, 1));
		list.add(new ItemStack(DarkBlocks.blockRuneInvitation, 1));

		list.add(new ItemStack(DarkBlocks.blockAltar, 1));
	}
}
