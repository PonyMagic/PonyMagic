package net.braunly.ponymagic.race;

public enum EnumRace {
	REGULAR("Регуляр", new String[]{}),
	PEGAS("Пегас", new String[]{
			"speed", "flyspeed",
			"flydurability", "slowfall",
			 "haste",
			 "staminaPool", "staminaRegen", "staminaHealthRegen", "staminaFoodRegen"}),
	UNICORN("Единорог", new String[]{
			"fireresistance", "hpregen", "unenchant",
			"solidcore", "tpbed",
			"shield", "heal", "enchant",
			"slowfall",
			 "staminaPool", "staminaRegen", "staminaHealthRegen", "staminaFoodRegen"}),
	EARTHPONY("Земнопони", new String[]{
			"jump", "grow",
			"stepup",
			"speed", "strength",
			"haste",
			"solidcore",
			 "staminaPool", "staminaRegen", "staminaHealthRegen", "staminaFoodRegen"}),
	ZEBRA("Зебра", new String[]{ "jump",
			"dispel", "fireresistance", "drown",
			"slow", "purity", "nightvision",
			"vulnerable", "antidote", "climb",
			 "staminaPool", "staminaRegen", "staminaHealthRegen", "staminaFoodRegen"});
	

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
