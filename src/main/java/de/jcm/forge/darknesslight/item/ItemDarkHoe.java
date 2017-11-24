package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemDarkHoe extends ItemHoe implements ICraftable
{
	
	public ItemDarkHoe()
	{
		super(DarknessVsLight.toolMaterialEngerium);
		setUnlocalizedName("energium_hoe");
		setTextureName(DarknessVsLight.MODID + ":" + "energium_hoe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aa#", "#b#", "#b#", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#aa", "##b", "##b", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
	}
}
