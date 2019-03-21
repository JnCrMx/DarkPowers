package de.jcm.darkpowers.item;

import de.jcm.darkpowers.DarkPowers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBookOfDarkMyths extends Item
{
	public static final int PARTS = 1;
	
	public ItemBookOfDarkMyths()
	{
		setTextureName(DarkPowers.MODID + ":" + "book_of_dark_myths");
		setUnlocalizedName("book_of_dark_myths");
        setMaxStackSize(1);
	}
	
	public String getItemStackDisplayName(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            if(nbttagcompound.hasKey("part"))
            {
	            int part = nbttagcompound.getInteger("part");
	            
	            return StatCollector.translateToLocalFormatted(getUnlocalizedName()+".part.name", part+1, PARTS);
            }
        }

        return StatCollector.translateToLocal(getUnlocalizedName()+".name");
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		NBTTagCompound tag;
		if(stack.hasTagCompound())
        {
			tag=stack.getTagCompound();
        }
		else
		{
			stack.setTagCompound(tag=new NBTTagCompound());
		}
		int part;
		if(!tag.hasKey("part"))
		{
			tag.setInteger("part", part=itemRand.nextInt(PARTS));
		}
		
		player.openGui(DarkPowers.instance, 1, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		
		return stack;
	}
	
	@Override
	public boolean getShareTag()
	{
		return true;
	}
}
