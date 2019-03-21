package de.jcm.darkpowers.ritual;

import java.util.function.Supplier;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.PlayerData.DarkRole;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.network.PacketClientEffect;
import de.jcm.darkpowers.tileentity.TileEntityAltar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class RitualSummoning extends Ritual
{
	public RitualSummoning(Supplier<EntityPlayer> player, TileEntityAltar altar, int x, int y, int z)
	{
		super(player, altar, x, y, z);
		
		addAction(new RitualActionLabel("start"));
		addAction(new RitualAction(false, a->
		{
			DarkPowers.wrapper.sendTo(new PacketClientEffect(a.player.get(), ClientEffect.DARKNESS, 1), (EntityPlayerMP) a.player.get());
			return 0;
		}));
		addAction(new RitualAction(false, a->{a.player.get().setPositionAndUpdate(x+5, a.world.get().getHeightValue(x+5, z), z); return 0;}));
		for(int i=1;i<=15;i++)
		{
			addAction(RitualAction.CHAT("ritual.summoning.text."+i));
			if(i==9)
				addAction(new RitualActionLabel("no-return"));
			addAction(RitualAction.SLEEP(20));
		}
		addAction(new RitualAction(false, a->{a.data.setDouble("distance", a.player.get().getDistance(a.x, a.y, a.z)); return 0;}));
		addAction(new RitualAction(true, a->
		{
			double distance0=a.data.getDouble("distance");
			double distance=a.player.get().getDistance(a.x, a.y, a.z);
			if(distance>distance0+2.5)
				return a.findLabel("no");
			if(distance<distance0-2.5)
				return a.findLabel("yes");
			return a.action;
		}));
		
		addAction(new RitualActionLabel("yes"));
		for(int i=1;i<=5;i++)
		{
			addAction(RitualAction.CHAT("ritual.summoning.yes."+i));
			addAction(RitualAction.SLEEP(20));
		}
		addAction(new RitualAction(false, a->
		{
			PlayerData data=PlayerData.get(player.get());
			//TODO: Revert
			//data.setRole(DarkRole.SERVANT);
			data.setRole(DarkRole.MAGE);
			data.setMaxEnergy(1000);
			data.sync();
			//TODO: Give achievement
			//TODO: Introduce mission & quests
			return 0;
		}));
		addAction(new RitualAction(false, a->
		{
			DarkPowers.wrapper.sendTo(new PacketClientEffect(a.player.get(), ClientEffect.DARKNESS, 0), (EntityPlayerMP) a.player.get());
			return 0;
		}));
		addAction(RitualAction.EXIT());
		
		addAction(new RitualActionLabel("no"));
		for(int i=1;i<=4;i++)
		{
			addAction(RitualAction.CHAT("ritual.summoning.no."+i));
			addAction(RitualAction.SLEEP(20));
		}
		addAction(new RitualAction(false, a->
		{
			//TODO: Start boss fight
			a.player.get().attackEntityFrom(DamageSource.magic, Float.MAX_VALUE);
			return 0;
		}));
		addAction(new RitualAction(false, a->
		{
			DarkPowers.wrapper.sendTo(new PacketClientEffect(a.player.get(), ClientEffect.DARKNESS, 0), (EntityPlayerMP) a.player.get());
			return 0;
		}));
		addAction(RitualAction.EXIT());
		
		addAction(new RitualActionLabel("cancel"));
		addAction(RitualAction.IF(r->r.cancelPoint<r.findLabel("no-return"), "leave", "no"));
		
		addAction(new RitualActionLabel("leave"));
		for(int i=1;i<=4;i++)
		{
			addAction(RitualAction.CHAT("ritual.summoning.leave."+i));
			addAction(RitualAction.SLEEP(20));
		}
		addAction(new RitualAction(false, a->
		{
			DarkPowers.wrapper.sendTo(new PacketClientEffect(a.player.get(), ClientEffect.DARKNESS, 0), (EntityPlayerMP) a.player.get());
			return 0;
		}));
		addAction(RitualAction.EXIT());
		
		setEntryPoint(findLabel("start"));
		setOnCancel(findLabel("cancel"));
	}

	@Override
	public boolean checkPreconditions()
	{
		return true;
	}

	@Override
	public double getRadius()
	{
		return 10;
	}
}
