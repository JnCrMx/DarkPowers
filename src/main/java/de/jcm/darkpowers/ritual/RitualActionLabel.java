package de.jcm.darkpowers.ritual;

import de.jcm.util.Callback;

public class RitualActionLabel extends RitualAction
{
	private String name;

	public RitualActionLabel(String name)
	{
		super(false, new Callback<Integer, Ritual>()
		{

			@Override
			public Integer call(Ritual argument)
			{
				return 0;
			}
		});
		this.name=name;
	}
	
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "RitualActionLabel [name=" + name + "]";
	}
	
}
