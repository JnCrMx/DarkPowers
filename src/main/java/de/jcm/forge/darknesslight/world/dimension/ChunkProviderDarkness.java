package de.jcm.forge.darknesslight.world.dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

public class ChunkProviderDarkness implements IChunkProvider
{
	private World worldObj;
	private NoiseGeneratorSimplex noiseGeneratorSimplexPositiv;
	private NoiseGeneratorSimplex noiseGeneratorSimplexNegativ;
	
	public ChunkProviderDarkness(World p_i2004_1_, long p_i2004_2_, boolean p_i2004_4_, String p_i2004_5_)
	{
		this.worldObj = p_i2004_1_;
		this.noiseGeneratorSimplexPositiv = new NoiseGeneratorSimplex(new Random());
		this.noiseGeneratorSimplexNegativ = new NoiseGeneratorSimplex(new Random());
	}
	
	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
	{
		return this.provideChunk(p_73158_1_, p_73158_2_);
	}
	
	@Override
	public Chunk provideChunk(int cx, int cz)
	{
		Block[] blocks = new Block[32768];
		byte[] bytes = new byte[32768];
				
		Chunk chunk = new Chunk(worldObj, blocks, bytes, cx, cz);
		
		chunk.generateSkylightMap();
		
		byte[] biomes = new byte[256];
		
		for (int i = 0; i < biomes.length; i++)
			biomes[i] = (byte) BiomeGenBase.sky.biomeID;
		
		chunk.setBiomeArray(biomes);
		return chunk;
	}
	
	/**
	 * Checks to see if a chunk exists at x, y
	 */
	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_)
	{
		return true;
	}
	
	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
	{
		
	}
	
	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If
	 * passed false, save up to two chunks. Return true if all chunks have been
	 * saved.
	 */
	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
	{
		return true;
	}
	
	/**
	 * Save extra data not associated with any Chunk. Not saved during autosave,
	 * only during world unload. Currently unimplemented.
	 */
	@Override
	public void saveExtraData()
	{
	}
	
	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}
	
	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	@Override
	public boolean canSave()
	{
		return true;
	}
	
	/**
	 * Converts the instance data to a readable string.
	 */
	@Override
	public String makeString()
	{
		return "Darkness";
	}
	
	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 */
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_)
	{
		return new ArrayList();
	}
	
	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}
	
	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_)
	{
		return null;
	}
	
	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_)
	{
		
	}
}