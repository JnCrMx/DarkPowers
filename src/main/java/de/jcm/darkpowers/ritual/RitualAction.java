package de.jcm.darkpowers.ritual;

import java.util.function.Predicate;

import de.jcm.util.Callback;

import net.minecraft.util.ChatComponentTranslation;

public class RitualAction
{		
	public static final RitualAction SLEEP(int ticks)
	{
		return new RitualAction(false, new Callback<Integer, Ritual>()
		{
			@Override
			public Integer call(Ritual argument)
			{
				if(argument.data.hasKey("sleepTimer"))
				{
					int sleepTimer = argument.data.getInteger("sleepTimer");
					sleepTimer--;
					argument.data.setInteger("sleepTimer", sleepTimer);
					if(sleepTimer==0)
					{
						argument.data.removeTag("sleepTimer");
						return 0;
					}
				}
				else
				{
					argument.data.setInteger("sleepTimer", ticks);
				}
				return 1;
			}
		});
	}
	
	public static final RitualAction CHAT(String key)
	{
		return new RitualAction(false, new Callback<Integer, Ritual>()
		{
			@Override
			public Integer call(Ritual argument)
			{
				argument.player.get().addChatMessage(new ChatComponentTranslation(key, argument.player.get().getCommandSenderName()));
				return 0;
			}
		});
	}
	
	public static final RitualAction JUMP(String label)
	{
		return new RitualAction(true, a->a.findLabel(label));
	}
	
	public static final RitualAction EXIT()
	{
		return new RitualAction(false, a->2);
	}
	
	@Override
	public String toString()
	{
		return "RitualAction [jump=" + jump + ", callback=" + callback + "]";
	}

	public static final RitualAction IF(Predicate<Ritual> condition, String _true, String _false)
	{
		return new RitualAction(true, a->condition.test(a)?a.findLabel(_true):a.findLabel(_false));
	}
	
	private boolean jump;
	private Callback<Integer, Ritual> callback;
	
	public RitualAction(boolean jump, Callback<Integer, Ritual> callback)
	{
		this.jump = jump;
		this.callback = callback;
	}
	
	public int execute(Ritual ritual)
	{
		if(jump)
			return callback.call(ritual);
		else
		{
			int exitCode = callback.call(ritual);
			switch(exitCode)
			{
				case 0:
					return ritual.action+1;
				case 1:
					return ritual.action;
				case 2:
					return -1;
				default:
					return ritual.action;
			}
		}
	}
}
