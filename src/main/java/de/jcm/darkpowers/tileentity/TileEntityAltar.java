package de.jcm.darkpowers.tileentity;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.function.Supplier;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.ritual.Ritual;
import de.jcm.darkpowers.util.PlayerReferenceHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAltar extends TileEntity
{
	public UUID activePlayer;
	public Ritual activeRitual;
	
	private WeakReference<EntityPlayer> playerReference;
	public Supplier<EntityPlayer> player = new Supplier<EntityPlayer>()
	{
		@Override
		public EntityPlayer get()
		{
			if(playerReference!=null && playerReference.get()!=null)
			{
				return playerReference.get();
			}
			else
			{
				return (playerReference=new WeakReference(PlayerReferenceHelper.findPlayer(activePlayer))).get();
			}
		}
	};
	
	public TileEntityAltar()
	{
		
	}
	
	@Override
	public void updateEntity()
	{
		if(player.get()!=null)
		{
			if(activeRitual!=null)
			{
				if(activeRitual.isActive())
				{
					if(player.get().getDistance(xCoord, yCoord, zCoord)>activeRitual.getRadius())
					{
						if(!activeRitual.isCanceled())
						{
							activeRitual.cancel();
						}
					}
					activeRitual.tick();
				}
				else
				{
					activeRitual=null;
					activePlayer=null;
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		
		if(activePlayer!=null && activeRitual!=null)
		{
			tag.setString("player", activePlayer.toString());
			tag.setString("ritual", Ritual.getName(activeRitual.getClass()));
			
			NBTTagCompound ritualData = new NBTTagCompound();
			activeRitual.writeToNBT(ritualData);
			tag.setTag("ritualData", ritualData);
		}
		DarkPowers.logger.debug("Wrote Altar NBT Tag: "+tag.toString());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		if(tag.hasKey("player"))
		{
			activePlayer = UUID.fromString(tag.getString("player"));
			if(activePlayer!=null)
			{
				if(tag.hasKey("ritual"))
				{
					String ritual = tag.getString("ritual");
					activeRitual = Ritual.createRitual(ritual, player, this, xCoord, yCoord, zCoord);
					if(activeRitual!=null)
					{
						activeRitual.readFromNBT(tag.getCompoundTag("ritualData"));
					}
				}
			}
		}
		DarkPowers.logger.debug("Read Altar NBT Tag: "+tag.toString());
	}
}
