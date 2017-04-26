package net.braunly.ponymagic.race;

import java.util.ArrayList;
import java.util.List;

public enum EnumRace {
	REGULAR("Регуляр", new String[]{}),
	PEGAS("Пегас", new String[]{
			"speed", "flyspeed",
			"flydurability", "slowfall",
			 "haste"}),
	UNICORN("Единорог", new String[]{
			"fireresistance", "hpregen", "unenchant",
			"solidcore", "tpbed",
			"shield", "heal", "enchant",
			"slowfall"}),
	EARTHPONY("Земнопони", new String[]{
			"jump", "grow",
			"stepup",
			"speed", "strength",
			"haste",
			"solidcore"}),
	ZEBRA("Зебра", new String[]{  // Jump
			"dispel", "fireresistance", "drown",
			"slow", "purity", "nightvision",
			"vulnerable", "antidote", "climb"});
	

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
