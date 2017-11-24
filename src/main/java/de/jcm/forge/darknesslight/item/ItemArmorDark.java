package de.jcm.forge.darknesslight.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.DarknessVsLight;
import de.jcm.forge.darknesslight.ICraftable;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemArmorDark extends ItemArmor implements ICraftable
{
	private String textureName;
	
	public ItemArmorDark(String unlocalizedName, int type)
	{
		super(DarknessVsLight.armorMaterialEngergium, 0, type);
		this.textureName = "energium";
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(DarknessVsLight.MODID + ":" + unlocalizedName);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return DarknessVsLight.MODID + ":textures/armor/" + this.textureName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	
	@Override
	public void registerRecipe()
	{
		if (this.armorType == 0)
		{
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "a#a", "###", 'a', DarknessVsLight.itemDarkIngot);
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "###", "aaa", "a#a", 'a', DarknessVsLight.itemDarkIngot);
		}
		if (this.armorType == 1)
		{
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a#a", "aaa", "aaa", 'a', DarknessVsLight.itemDarkIngot);
		}
		if (this.armorType == 2)
		{
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "aaa", "a#a", "a#a", 'a', DarknessVsLight.itemDarkIngot);
		}
		if (this.armorType == 3)
		{
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "a#a", "a#a", "###", 'a', DarknessVsLight.itemDarkIngot);
			GameRegistry.addShapedRecipe(new ItemStack(this, 1), "###", "a#a", "a#a", 'a', DarknessVsLight.itemDarkIngot);
		}
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		
		return super.getArmorModel(entityLiving, itemStack, armorSlot);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		/*if (player.getCurrentArmor(0) != null && player.getCurrentArmor(1) != null && player.getCurrentArmor(2) != null && player.getCurrentArmor(3) != null) if (player.getCurrentArmor(3).getItem() == DarknessVsLight.itemEnergiumHelmet)
		{
			if (player.getCurrentArmor(2).getItem() == DarknessVsLight.dark)
			{
				if (player.getCurrentArmor(1).getItem() == DarknessVsLight.itemEnergiumLeggings)
				{
					if (player.getCurrentArmor(0).getItem() == DarknessVsLight.itemEnergiumBoots)
					{
						player.addPotionEffect(new PotionEffect(10, 5, 3, true));
						player.addPotionEffect(new PotionEffect(23, 5, 5, true));
					}
				}
			}
		}
		
		if (player.getCurrentArmor(3) != null) if (player.getCurrentArmor(3).getItem() == DarknessVsLight.itemEnergiumHelmet)
		{
			if (!player.isPotionActive(16)) player.addPotionEffect(new PotionEffect(16, 500, 10, true));
		}
		if (player.getCurrentArmor(2) != null) if (player.getCurrentArmor(2).getItem() == DarknessVsLight.itemEnergiumChestplate)
		{
			player.addPotionEffect(new PotionEffect(3, 5, 3, true));
		}
		if (player.getCurrentArmor(1) != null) if (player.getCurrentArmor(1).getItem() == DarknessVsLight.itemEnergiumLeggings)
		{
			player.addPotionEffect(new PotionEffect(1, 5, 1, true));
		}
		if (player.getCurrentArmor(0) != null) if (player.getCurrentArmor(0).getItem() == DarknessVsLight.itemEnergiumBoots)
		{
			player.addPotionEffect(new PotionEffect(8, 5, 2, true));
		}
		
		super.onArmorTick(world, player, itemStack);*/ //TODO: fix
	}
}
