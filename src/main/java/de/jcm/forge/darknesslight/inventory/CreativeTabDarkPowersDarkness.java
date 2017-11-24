package de.jcm.forge.darknesslight.inventory;

import java.util.List;

import de.jcm.forge.darknesslight.DarknessVsLight;
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
		return Item.getItemFromBlock(DarknessVsLight.blockDarkness);
	}
	
	// Force item sorting
	// Standard sorting still not working, used again
	@Override
	public void displayAllReleventItems(List list)
	{
		list.add(new ItemStack(DarknessVsLight.itemDarkIngot, 1));
		list.add(new ItemStack(DarknessVsLight.blockDarkness, 1));
		
		list.add(new ItemStack(DarknessVsLight.blockRuneEmpty, 1));
		list.add(new ItemStack(DarknessVsLight.blockRuneBinding, 1));
		list.add(new ItemStack(DarknessVsLight.blockRuneOpening, 1));
		list.add(new ItemStack(DarknessVsLight.blockRuneHolding, 1));
		list.add(new ItemStack(DarknessVsLight.blockRuneInvitation, 1));
		
		list.add(new ItemStack(DarknessVsLight.blockRitualTable, 1));
		
		list.add(new ItemStack(DarknessVsLight.itemDarkSword, 1));
		// TODO: Add armor and tools here
	}
}
