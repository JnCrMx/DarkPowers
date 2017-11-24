package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemDarkPickaxe extends ItemPickaxe implements ICraftable
{
	
	public ItemDarkPickaxe()
	{
		super(DarknessVsLight.toolMaterialEngerium);
		setUnlocalizedName("energium_pickaxe");
		setTextureName(DarknessVsLight.MODID + ":" + "energium_pickaxe");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "#b#", "#b#", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
	}
}
