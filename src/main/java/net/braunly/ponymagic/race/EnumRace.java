package net.braunly.ponymagic.race;

import java.util.ArrayList;
import java.util.List;

public enum EnumRace {
	REGULAR("Регуляр", new String[]{}),
	PEGAS("Пегас", new String[]{""}),
	UNICORN("Единорог", new String[]{""}),
	EARTHPONY("Земнопони", new String[]{""}),
	ZEBRA("Зебра", new String[]{"antidote", "climbing"});
	

	private final String[] spells;
	private final String localizedName;

	private EnumRace(String localizedName, String[] spells) {
		this.localizedName = localizedName;  // TODO lang file
		this.spells = spells;
	}

	
	public static EnumRace getById(Integer id) {
		return EnumRace.values()[id];
	}
	
	public static EnumRace getByName(String name) {
		for (EnumRace race: EnumRace.values()) {
			if (race.name().equalsIgnoreCase(name)) {
				return race;
			}
		}
		return null;
	}
	
	public String getLocalizedName() {
		return this.localizedName;
	}
	
	public String[] getSpells() {
		return this.spells;
	}

}
