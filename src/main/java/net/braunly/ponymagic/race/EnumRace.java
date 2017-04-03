package net.braunly.ponymagic.race;

public enum EnumRace {
	Regular, Pegas, Unicorn, Earthpony, Zebra;
	
//	regular("Regular", 0),
//	pegas("Pegas", 1),
//	unicorn("Unicorn", 2),
//	earthpony("Earthpony", 3),
//	zebra("Zebra", 4);
//	
//	private final String name;
//	private final Integer id;
//	
//	private EnumRace(String name, Integer id) {
//		this.name = name;
//		this.id = id;
//	}
//	
//	public int getId() {
//		return this.id;
//	}
//	
//	public String getName() {
//		return this.name();
//	}
	
	public static EnumRace getById(Integer id) {
		return EnumRace.values()[id];
	}
	
	public static EnumRace getByName(String name) {
		for (EnumRace race: EnumRace.values()) {
			if (race.name().equalsIgnoreCase(name)) {
				return race;
			}
		}
		return EnumRace.Regular;
	}

}
