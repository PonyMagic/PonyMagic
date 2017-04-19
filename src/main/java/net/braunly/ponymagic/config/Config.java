package net.braunly.ponymagic.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean expModifier;
	public static float expModifierAmount;
	
	public static float defaultStaminaPool;
	public static float defaultStaminaRegen;
	public static float lowFoodStaminaRegen;
	public static float waterStaminaRegen;
	public static boolean burnStaminaWhenHungry;
	public static int highFoodLevel;
	public static int lowFoodLevel;
	
	public static Map<String, Integer[]> potions = new HashMap<String, Integer[]>();
	public static Map<String, Integer[]> spells = new HashMap<String, Integer[]>();
	
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		// Опыт
		expModifier = config.getBoolean("expModifier", "Опыт", false, "Использовать модификатор опыта?");
		expModifierAmount = config.getFloat("expModifierAmount", "Опыт", 1.0F, 0.0F, 100.0F, "Модификатор опыта");
		
		// Stamina
		defaultStaminaPool = config.getFloat("defaultStaminaPool", "Стамина", 100.0F, 0.0F, 1000.0F, "Стандартное значение стамины");
		defaultStaminaRegen = config.getFloat("defaultStaminaRegen", "Стамина", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		waterStaminaRegen = config.getFloat("waterStaminaRegen", "Стамина", 0.025F, 0.0F, 100.0F, "Реген в воде в тик");
		burnStaminaWhenHungry = config.getBoolean("burnStaminaWhenHungry", "Стамина", true, "Обнулить стамину при голоде");
		lowFoodStaminaRegen = config.getFloat("lowFoodStaminaRegen", "Стамина", 0.05F, 0.0F, 100.0F, "Реген в тик при низком голоде");
		highFoodLevel = config.getInt("highFoodLevel", "Стамина", 12, 0, 20, "Уменьшить реген, если голода меньше");
		lowFoodLevel = config.getInt("lowFoodLevel", "Стамина", 6, 0, 20, "Отключить реген, если голода меньше");
		
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
		potions.put("climb", new Integer[] {
				config.getInt("climbDur", "Potions", 60, 0, 600, "Длительность climb. (сек)"),
				config.getInt("climbStamina", "Potions", 30, 0, 100, "Стамина за climb."),
				config.getInt("climbLvl", "Potions", 1, 1, 5, "Уровень эффекта climb.")
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
		potions.put("drown", new Integer[] {
				config.getInt("drownDur", "Potions", 60, 0, 600, "Длительность drown. (сек)"),
				config.getInt("drownStamina", "Potions", 30, 0, 100, "Стамина за drown."),
				config.getInt("drownLvl", "Potions", 1, 1, 5, "Уровень эффекта drown.")
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
		potions.put("vulnerable", new Integer[] {
				config.getInt("vulnerableDur", "Potions", 30, 0, 600, "Длительность vulnerable. (сек)"),
				config.getInt("vulnerableStamina", "Potions", 30, 0, 100, "Стамина за vulnerable."),
				config.getInt("vulnerableLvl", "Potions", 1, 1, 5, "Уровень эффекта vulnerable.")
		});
		potions.put("stepUp", new Integer[] {
				config.getInt("stepUpDur", "Potions", 60, 0, 600, "Длительность stepUp. (сек)"),
				config.getInt("stepUpStamina", "Potions", 30, 0, 100, "Стамина за stepUp."),
				config.getInt("stepUpLvl", "Potions", 2, 1, 5, "Уровень эффекта stepUp.")
		});
		
		
		// Spells
		spells.put("grow", new Integer[] {
				config.getInt("growStamina", "Spells", 10, 0, 100, "Стамина за grow."),
		});
		
		config.save();
	}
}
