package de.jcm.forge.darknesslight.util;

import net.minecraft.block.Block;

public class ChunkHelper
{
	public static Block[] setBlock(Block[] blocks, int x, int y, int z, Block block, boolean override)
	{
		if (blocks[x << 11 | z << 7 | y] == null || override) blocks[x << 11 | z << 7 | y] = block;
		return blocks;
	}
	
	public static Block[] setBlock(Block[] blocks, int x, int y, int z, Block block)
	{
		blocks[x << 11 | z << 7 | y] = block;
		return blocks;
	}
	
	public static Block[] setBlock(Block[] blocks, int x, int y, int z, Block block, Block... not)
	{
		if (blocks[x << 11 | z << 7 | y] != null) for (Block block2 : not)
		{
			if (blocks[x << 11 | z << 7 | y] == block2)
			{
				return blocks;
			}
		}
		
		blocks[x << 11 | z << 7 | y] = block;
		return blocks;
	}
	
	public static byte[] setMeta(byte[] metas, int x, int y, int z, byte meta)
	{
		metas[x << 11 | z << 7 | y] = meta;
		return metas;
	}
	
	public static Block[] fillBlocks(Block[] blocks, int x1, int y1, int z1, int x2, int y2, int z2, Block block)
	{
		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				for (int z = z1; z <= z2; z++)
				{
					blocks = setBlock(blocks, x, y, z, block);
				}
			}
		}
		return blocks;
	}
	
	public static Block[] fillBlocks(Block[] blocks, int x1, int y1, int z1, int x2, int y2, int z2, Block block, Block... nots)
	{
		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				for (int z = z1; z <= z2; z++)
				{
					blocks = setBlock(blocks, x, y, z, block, nots);
				}
			}
		}
		return blocks;
	}
	
	public static Block[] fillBlocks(Block[] blocks, int x1, int y1, int z1, int x2, int y2, int z2, Block block, boolean override)
	{
		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				for (int z = z1; z <= z2; z++)
				{
					blocks = setBlock(blocks, x, y, z, block, override);
				}
			}
		}
		return blocks;
	}
	
	public static byte[] fillMetas(byte[] metas, int x1, int y1, int z1, int x2, int y2, int z2, byte meta)
	{
		for (int x = x1; x <= x2; x++)
		{
			for (int y = y1; y <= y2; y++)
			{
				for (int z = z1; z <= z2; z++)
				{
					metas = setMeta(metas, x, y, z, meta);
				}
			}
		}
		return metas;
	}
}
