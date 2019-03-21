package de.jcm.darkpowers.ritual;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.tileentity.TileEntityAltar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class Ritual
{	
	private static final BiMap<String, Class<? extends Ritual>> rituals = HashBiMap.create();
	
	public static void registerRitual(String name, Class<? extends Ritual> ritual)
	{
		if(rituals.containsKey(name))
			throw new IllegalArgumentException("a ritual with this name is already registered");
		
		rituals.put(name, ritual);
	}
	
	public static Class<? extends Ritual> getRitualClass(String name)
	{
		return rituals.getOrDefault(name, null);
	}
	
	public static String getName(Class<? extends Ritual> ritualClass)
	{
		return rituals.inverse().getOrDefault(ritualClass, null);
	}
	
	public static Set<String> getNames()
	{
		return rituals.keySet();
	}
	
	public static Ritual createRitual(String name, Supplier<EntityPlayer> player, TileEntityAltar altar, int x, int y, int z)
	{
		Class clazz = getRitualClass(name);
		if(clazz==null)
		{
			DarkPowers.logger.error("Cannot find ritual named "+name+"!");
			return null;
		}
		
		try
		{
			Constructor<? extends Ritual> constructor = clazz.getConstructor(Supplier.class, TileEntityAltar.class, int.class, int.class, int.class);
			return constructor.newInstance(player, altar, x, y, z);
		}
		catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			DarkPowers.logger.error("Cannot find or invoke constructor "+clazz.getSimpleName()+"(Supplier<EntityPlayer> player, int x, int y, int z) for ritual "+name+"!", e);
			return null;
		}
	}
	
	public Supplier<EntityPlayer> player;
	public Supplier<World> world;
	public int x, y, z;
	public TileEntityAltar altar;
	
	public HashMap<Integer, RitualAction> actions = new HashMap<>();
	public NBTTagCompound data = new NBTTagCompound();
	public int action;
	
	private boolean active;
	
	private int entryPoint;
	private int onCancel;
	public int cancelPoint = -1;
	
	public Ritual(Supplier<EntityPlayer> player, TileEntityAltar altar, int x, int y, int z)
	{
		this.player = player;
		this.world = ()->player.get().worldObj;
		this.x = x;
		this.y = y;
		this.z = z;
		this.altar = altar;
	}
	
	/**
	 * Add an action whose position is smaller than the position of every other
	 * action currently present.
	 * 
	 * @param e
	 */
	public void addFirstAction(RitualAction e)
	{
		if(actions.isEmpty())
		{
			actions.put(0, e);
		}
		else
		{
			actions.put(actions.keySet().stream().min(Comparator.naturalOrder()).get() - 1, e);
		}
	}
	
	/**
	 * Add an action whose position is larger than the position of every other
	 * action currently present.
	 * 
	 * @param e
	 */
	public void addLastAction(RitualAction e)
	{
		if(actions.isEmpty())
		{
			actions.put(0, e);
		}
		else
		{
			actions.put(actions.keySet().stream().max(Comparator.naturalOrder()).get() + 1, e);
		}
	}
	
	/**
	 * Add an action whose position is larger than the position of every other
	 * action currently present.
	 * 
	 * @param e
	 */
	public void addAction(RitualAction e)
	{
		addLastAction(e);
	}
	
	public void clearActions()
	{
		actions.clear();
	}
	
	public RitualAction getAction(int index)
	{
		return actions.get(index);
	}
	
	public void addAction(int index, RitualAction element)
	{
		actions.put(index, element);
	}
	
	public RitualAction removeFirstAction()
	{
		return actions.remove(actions.keySet().stream().min(Comparator.naturalOrder()).get());
	}
	
	public RitualAction removeLastAction()
	{
		return actions.remove(actions.keySet().stream().max(Comparator.naturalOrder()).get());
	}
	
	public RitualAction removeAction(int index)
	{
		return actions.remove(index);
	}
	
	public void start()
	{
		active = true;
		action = entryPoint;
	}
	
	public void stop()
	{
		active = false;
	}
	
	public void cancel()
	{
		cancelPoint = action;
		action = onCancel;
		if(action < 0 || action >= actions.size() - 1)
		{
			active = false;
		}
		altar.markDirty();
	}
	
	public boolean isCanceled()
	{
		return cancelPoint!=-1;
	}
	
	public HashMap<Integer, RitualAction> getActions()
	{
		return actions;
	}

	public void setActions(HashMap<Integer, RitualAction> actions)
	{
		this.actions = actions;
	}

	public boolean isActive()
	{
		return active;
	}

	public void tick()
	{
		if(active)
		{
			action = getAction(action).execute(this);
			if(action < 0 || action >= actions.size() - 1)
			{
				active = false;
			}
			altar.markDirty();
		}
	}
	
	public int getEntryPoint()
	{
		return entryPoint;
	}
	
	public void setEntryPoint(int entryPoint)
	{
		this.entryPoint = entryPoint;
	}
	
	public int getOnCancel()
	{
		return onCancel;
	}
	
	public void setOnCancel(int onCancel)
	{
		this.onCancel = onCancel;
	}
	
	public int findLabel(String name)
	{
		Optional<Entry<Integer, RitualAction>> label = actions.entrySet().stream()
			.filter(e -> e.getValue() instanceof RitualActionLabel && ((RitualActionLabel) e.getValue()).getName().equals(name))
			.findFirst();
		if(label.isPresent())
		{
			return label.get().getKey();
		}
		else
		{
			return -1;
		}
	}
	
	public abstract boolean checkPreconditions();
	public abstract double getRadius();
	
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("position", action);
		tag.setInteger("cancelPoint", cancelPoint);
		tag.setBoolean("active", active);
		tag.setTag("data", data);
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		action = tag.getInteger("position");
		cancelPoint = tag.getInteger("cancelPoint");
		active = tag.getBoolean("active");
		data = tag.getCompoundTag("data");
	}
}
