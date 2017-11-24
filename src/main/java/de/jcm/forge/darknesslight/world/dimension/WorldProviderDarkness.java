package de.jcm.forge.darknesslight.world.dimension;

import de.jcm.forge.darknesslight.DarknessVsLight;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderDarkness extends WorldProvider
{
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderDarkness(worldObj, 0, true, "");
	}
	
	@Override
	public String getDimensionName()
	{
		return "Darkness";
	}
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z)
	{
		return BiomeGenBase.sky;
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	@Override
	public boolean canSnowAt(int x, int y, int z, boolean checkLight)
	{
		return false;
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return "dimension.darkness.welcome";
	}
	
	@Override
	public ChunkCoordinates getSpawnPoint()
	{
		return new ChunkCoordinates(6, 41, 6);
	}
}
