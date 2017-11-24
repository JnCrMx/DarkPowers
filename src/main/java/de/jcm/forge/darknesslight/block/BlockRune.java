package de.jcm.forge.darknesslight.block;

import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockRune extends Block
{
	public final RuneType type;
	
	public BlockRune(RuneType type)
	{
		super(Material.rock);
		this.type = type;
		setHardness(25.0f);
		setResistance(100.0f);
		setBlockName("rune_" + type.getSuffix());
		setBlockTextureName(DarknessVsLight.MODID + ":" + "rune_" + type.getSuffix());
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3)
	{
		if (!world.isRemote)
		{
			int meta = world.getBlockMetadata(x, y, z);
			
			if (meta == 0)
			{
				player.attackEntityFrom(DamageSource.magic, 10.0f);
				player.addChatComponentMessage(new ChatComponentTranslation("messages.rune.activated", new Object[] {}));
				world.setBlockMetadataWithNotify(x, y, z, 1, 3);
			}
			else
			{
				player.addChatComponentMessage(new ChatComponentTranslation("messages.rune.active", new Object[] {}));
			}
		}
		return true;
	}
	
	public static enum RuneType
	{
		EMPTY("empty"), BINDING("binding"), OPENING("opening"), HOLDING("holding"), INVITATION("invitation");
		
		private String suffix;
		
		private RuneType(String suffix)
		{
			this.suffix = suffix;
		}
		
		public String getSuffix()
		{
			return suffix;
		}
	}
}
