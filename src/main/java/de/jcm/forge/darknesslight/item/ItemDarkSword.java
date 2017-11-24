package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemDarkSword extends ItemSword implements ICraftable
{
	
	public ItemDarkSword()
	{
		super(DarknessVsLight.toolMaterialDark);
		setUnlocalizedName("dark_sword");
		setTextureName(DarknessVsLight.MODID + ":" + "dark_sword");
		setFull3D();
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a##", "a##", "b##", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "#a#", "#a#", "#b#", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
		GameRegistry.addShapedRecipe(new ItemStack(this, 1), "##a", "##a", "##b", 'a', DarknessVsLight.itemDarkIngot, 'b', Items.stick);
	}
}
