package net.braunly.ponymagic.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static float defaultStaminaPool;
	public static float defaultStaminaRegen;
	
	public static Map<String, Integer[]> potions = new HashMap<String, Integer[]>();
	
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		defaultStaminaPool = config.getFloat("defaultStaminaPool", "Главное", 100.0F, 0.0F, 1000.0F, "Стандартное значение стамины");
		defaultStaminaRegen = config.getFloat("defaultStaminaRegen", "Главное", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		
		// POTIONS
		potions.put("antidote", new Integer[] {
				config.getInt("antidoteDur", "Potions", 60, 0, 0, "Длительность antidote. (сек)"),
				config.getInt("antidoteStamina", "Potions", 30, 0, 0, "Стамина за antidote.")
		});
		
		config.save();
	}
}
