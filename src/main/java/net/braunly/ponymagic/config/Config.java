package net.braunly.ponymagic.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static float defaultStaminaPool;
	public static float defaultStaminaRegen;
	
	public static float lowFoodStaminaRegen;
	public static float waterStaminaRegen;
	
	public static boolean burnStaminaWhenHungry;
	public static int highFoodLevel;
	public static int lowFoodLevel;
	
	public static Map<String, Integer[]> potions = new HashMap<String, Integer[]>();
	
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		defaultStaminaPool = config.getFloat("defaultStaminaPool", "Главное", 100.0F, 0.0F, 1000.0F, "Стандартное значение стамины");
		defaultStaminaRegen = config.getFloat("defaultStaminaRegen", "Главное", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		waterStaminaRegen = config.getFloat("waterStaminaRegen", "Главное", 0.025F, 0.0F, 100.0F, "Реген в воде в тик");
		
		burnStaminaWhenHungry = config.getBoolean("burnStaminaWhenHungry", "Еда", true, "Обнулить стамину при голоде");
		lowFoodStaminaRegen = config.getFloat("lowFoodStaminaRegen", "Еда", 0.05F, 0.0F, 100.0F, "Реген в тик при низком голоде");
		highFoodLevel = config.getInt("highFoodLevel", "Еда", 12, 0, 20, "Уменьшить реген, если голода меньше");
		lowFoodLevel = config.getInt("lowFoodLevel", "Еда", 6, 0, 20, "Отключить реген, если голода меньше");
		
		// POTIONS
		potions.put("antidote", new Integer[] {
				config.getInt("antidoteDur", "Potions", 60, 0, 600, "Длительность antidote. (сек)"),
				config.getInt("antidoteStamina", "Potions", 30, 0, 100, "Стамина за antidote.")
		});
		
		config.save();
	}
}
