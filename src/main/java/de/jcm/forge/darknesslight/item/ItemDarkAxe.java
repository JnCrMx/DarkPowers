package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemDarkAxe extends ItemAxe implements ICraftable
{
	
	public ItemDarkAxe()
	{
		super(DarknessVsLight.toolMaterialEngerium);
		setUnlocalizedName("energium_axe");
		setTextureName(DarknessVsLight.MODID + ":" + "energium_axe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aa#", "ab#", "#b#", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#aa", "#ab", "#b", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
	}
}
