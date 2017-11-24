package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ItemDarkSpade extends ItemSpade implements ICraftable
{
	
	public ItemDarkSpade()
	{
		super(DarknessVsLight.toolMaterialEngerium);
		setUnlocalizedName("energium_shovel");
		setTextureName(DarknessVsLight.MODID + ":" + "energium_shovel");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a##", "b##", "b##", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#a#", "#b#", "#b#", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "##a", "##b", "##b", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
	}
	
}
