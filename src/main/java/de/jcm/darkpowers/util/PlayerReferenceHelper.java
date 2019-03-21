package de.jcm.darkpowers.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerReferenceHelper
{
	public static EntityPlayer findPlayer(UUID uuid)
	{
		return (EntityPlayer) MinecraftServer.getServer().getConfigurationManager().
			playerEntityList.stream().filter(p->((EntityPlayer)p).getUniqueID().equals(uuid)).findAny().orElse(null);
	}
}
