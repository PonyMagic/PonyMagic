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
		potions.put("jump", new Integer[] {
				config.getInt("jumpDur", "Potions", 60, 0, 600, "Длительность jump (сек)."),
				config.getInt("jumpStamina", "Potions", 30, 0, 100, "Стамина за jump."),
				config.getInt("jumpLvl", "Potions", 2, 1, 5, "Уровень эффекта jump.")
		});
		potions.put("antidote", new Integer[] {
				config.getInt("antidoteDur", "Potions", 60, 0, 600, "Длительность antidote. (сек)"),
				config.getInt("antidoteStamina", "Potions", 30, 0, 100, "Стамина за antidote."),
				config.getInt("antidoteLvl", "Potions", 1, 1, 5, "Уровень эффекта antidote.")
		});
		potions.put("climbing", new Integer[] {
				config.getInt("climbingDur", "Potions", 60, 0, 600, "Длительность climbing. (сек)"),
				config.getInt("climbingStamina", "Potions", 30, 0, 100, "Стамина за climbing."),
				config.getInt("climbingLvl", "Potions", 1, 1, 5, "Уровень эффекта climbing.")
		});
		potions.put("dispel", new Integer[] {
				config.getInt("dispelDur", "Potions", 1, 0, 600, "Длительность dispel. (сек)"),
				config.getInt("dispelStamina", "Potions", 60, 0, 100, "Стамина за dispel."),
				config.getInt("dispelLvl", "Potions", 1, 1, 5, "Уровень эффекта dispel.")
		});
		potions.put("fireResistance", new Integer[] {
				config.getInt("fireResistanceDur", "Potions", 90, 0, 600, "Длительность fireResistance. (сек)"),
				config.getInt("fireResistanceStamina", "Potions", 60, 0, 100, "Стамина за fireResistance."),
				config.getInt("fireResistanceLvl", "Potions", 1, 1, 5, "Уровень эффекта fireResistance.")
		});
		potions.put("drowning", new Integer[] {
				config.getInt("drowningDur", "Potions", 60, 0, 600, "Длительность drowning. (сек)"),
				config.getInt("drowningStamina", "Potions", 30, 0, 100, "Стамина за drowning."),
				config.getInt("drowningLvl", "Potions", 1, 1, 5, "Уровень эффекта drowning.")
		});
		potions.put("slow", new Integer[] {
				config.getInt("slowDur", "Potions", 30, 0, 600, "Длительность slow. (сек)"),
				config.getInt("slowStamina", "Potions", 30, 0, 100, "Стамина за slow."),
				config.getInt("slowLvl", "Potions", 1, 1, 5, "Уровень эффекта slow.")
		});
		potions.put("purity", new Integer[] {
				config.getInt("purityDur", "Potions", 60, 0, 600, "Длительность purity. (сек)"),
				config.getInt("purityStamina", "Potions", 30, 0, 100, "Стамина за purity."),
				config.getInt("purityLvl", "Potions", 1, 1, 5, "Уровень эффекта purity.")
		});
		potions.put("nightVision", new Integer[] {
				config.getInt("nightVisionDur", "Potions", 60, 0, 600, "Длительность nightVision. (сек)"),
				config.getInt("nightVisionStamina", "Potions", 30, 0, 100, "Стамина за nightVision."),
				config.getInt("nightVisionLvl", "Potions", 1, 1, 5, "Уровень эффекта nightVision.")
		});
		potions.put("vulnerability", new Integer[] {
				config.getInt("vulnerabilityDur", "Potions", 30, 0, 600, "Длительность vulnerability. (сек)"),
				config.getInt("vulnerabilityStamina", "Potions", 30, 0, 100, "Стамина за vulnerability."),
				config.getInt("vulnerabilityLvl", "Potions", 1, 1, 5, "Уровень эффекта vulnerability.")
		});
		
		config.save();
	}
}
