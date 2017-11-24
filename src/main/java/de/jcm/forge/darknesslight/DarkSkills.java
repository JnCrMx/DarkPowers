package de.jcm.forge.darknesslight;

import net.minecraft.client.resources.I18n;

public enum DarkSkills
{
	FLASH("flash", new String[]{}),
	JUMP("jump", new String[]{})
	;
	
	private String name;
	private String[] upgrades;
	
	private DarkSkills(String name, String[] upgrades)
	{
		this.name=name;
		this.upgrades=upgrades;
	}

	public String getName()
	{
		return name;
	}

	public String[] getUpgrades()
	{
		return upgrades;
	}
	
	public String getLocalizedName()
	{
		return I18n.format("skill.darkness."+getName()+".name", new Object[0]);
	}
	
	public String getLocalizedDescription()
	{
		return I18n.format("skill.darkness."+getName()+".description", new Object[0]);
	}
}
