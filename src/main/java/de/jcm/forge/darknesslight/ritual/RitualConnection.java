package de.jcm.forge.darknesslight.ritual;

import java.util.ArrayList;

import javax.vecmath.Point3i;

import de.jcm.forge.darknesslight.Constants;
import de.jcm.forge.darknesslight.PlayerData;
import de.jcm.forge.darknesslight.block.BlockRune;
import de.jcm.forge.darknesslight.block.BlockRune.RuneType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class RitualConnection extends Ritual
{
	
	public RitualConnection(EntityPlayer player, int x, int y, int z)
	{
		super(player, x, y, z);
	}
	
	@Override
	public void run()
	{
		EntityLightningBolt lightning = new EntityLightningBolt(world, x, y + 1, z);
		Minecraft.getMinecraft().theWorld.spawnEntityInWorld(lightning);
		
		ghostAppear(x, y + 2, z);
		
		PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
		if (data.isDarkness())
		{
			return;
		}
		
		ArrayList<ChatComponentTranslation> messages = new ArrayList<ChatComponentTranslation>();
		for (int i = 1; i <= 22; i++)
			messages.add(new ChatComponentTranslation("ritual.connection.text." + i, player.getCommandSenderName()));
		
		for (ChatComponentTranslation chatComponentTranslation : messages)
		{
			player.addChatComponentMessage(chatComponentTranslation);
		}
		
		ghostDisappear();
		finish();
	}
	
	private void ghostAppear(int x, int y, int z)
	{
		
	}
	
	private void ghostMove(int x, int y, int z)
	{
		
	}
	
	private void ghostDisappear()
	{
		
	}
	
	@Override
	public void onCancel() throws Exception
	{
		ChatComponentTranslation msg1 = new ChatComponentTranslation("ritual.connection.leave.1", player.getCommandSenderName());
		ChatComponentTranslation msg2 = new ChatComponentTranslation("ritual.connection.leave.2", player.getCommandSenderName());
		ChatComponentTranslation msg3 = new ChatComponentTranslation("ritual.connection.leave.3", player.getCommandSenderName());
		ChatComponentTranslation msg4 = new ChatComponentTranslation("ritual.connection.leave.4", player.getCommandSenderName());
		ChatComponentTranslation msg5 = new ChatComponentTranslation("ritual.connection.leave.5", player.getCommandSenderName());
		ChatComponentTranslation msg6 = new ChatComponentTranslation("ritual.connection.leave.6", player.getCommandSenderName());
		ChatComponentTranslation msg7 = new ChatComponentTranslation("ritual.connection.leave.7", player.getCommandSenderName());
		ChatComponentTranslation msg8 = new ChatComponentTranslation("ritual.connection.leave.8", player.getCommandSenderName());
		
		player.addChatComponentMessage(msg1);
		Thread.sleep(1000);
		player.addChatComponentMessage(msg2);
		Thread.sleep(2000);
		player.addChatComponentMessage(msg3);
		Thread.sleep(1000);
		player.addChatComponentMessage(msg4);
		Thread.sleep(1000);
		player.addChatComponentMessage(msg5);
		Thread.sleep(1000);
		player.addChatComponentMessage(msg6);
		Thread.sleep(1000);
		player.addChatComponentMessage(msg7);
		Thread.sleep(2000);
		player.addChatComponentMessage(msg8);
		
		ghostDisappear();
	}
	
	@Override
	public boolean isCompleted()
	{
		boolean ok = true;
		
		Point3i[] runes = { new Point3i(1, -1, 0), new Point3i(-1, -1, 0), new Point3i(0, -1, 1), new Point3i(0, -1, -1) };
		boolean binding = false, opening = false, holding = false, invite = false;
		for (Point3i p : runes)
		{
			Block runeBlock = world.getBlock(p.x + x, p.y + y, p.z + z);
			if (runeBlock instanceof BlockRune)
			{
				int meta = world.getBlockMetadata(p.x + x, p.y + y, p.z + z);
				if (meta == 1)
				{
					if (((BlockRune) runeBlock).type == RuneType.BINDING) binding = true;
					;
					if (((BlockRune) runeBlock).type == RuneType.HOLDING) holding = true;
					if (((BlockRune) runeBlock).type == RuneType.OPENING) opening = true;
					if (((BlockRune) runeBlock).type == RuneType.INVITATION) invite = true;
				}
				else
					ok = false;
			}
			else
				ok = false;
		}
		if (ok == true) ok = binding && opening && holding && invite;
		
		return ok;
	}
	
	public void finish()
	{
		PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
		data.setDarkness(true);
		data.setEnergy(Constants.MAX_ENERGY/2);
		data.sync();
	}
}
