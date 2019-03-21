package de.jcm.darkpowers.inventory;

import java.util.List;

import de.jcm.darkpowers.DarkPowers;

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
		return Item.getItemFromBlock(DarkPowers.blockDarkness);
	}
	
	// Force item sorting
	// Standard sorting still not working, used again
	@Override
	public void displayAllReleventItems(List list)
	{
		list.add(new ItemStack(DarkPowers.itemDarkIngot, 1));
		list.add(new ItemStack(DarkPowers.blockDarkness, 1));
		
		list.add(new ItemStack(DarkPowers.blockRuneEmpty, 1));
		list.add(new ItemStack(DarkPowers.blockRuneBinding, 1));
		list.add(new ItemStack(DarkPowers.blockRuneOpening, 1));
		list.add(new ItemStack(DarkPowers.blockRuneHolding, 1));
		list.add(new ItemStack(DarkPowers.blockRuneInvitation, 1));
		
		list.add(new ItemStack(DarkPowers.blockAltar, 1));
		
		list.add(new ItemStack(DarkPowers.itemDarkSword, 1));
		// TODO: Add armor and tools here
	}
}
