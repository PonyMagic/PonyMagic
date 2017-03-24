package net.braunly.ponymagic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static float defaultStamina;
	public static float regenerationValue;
	public static float lowFoodRegenValue;
	public static float waterRegenValue;
	
	public static boolean burnStaminaWhenHungry;
	public static int highFoodLevel;
	public static int lowFoodLevel;
	
	public static float flyEnableValue;
	public static float flyDisableValue;
	public static float flyHoverValue;
	public static float flySpendingValue;
	
	public static float potionsDamageResValue;
	public static float potionsFireResValue;
	public static float potionsHealValue;
	public static float potionsHungerValue;
	public static float potionsJumpValue;
	public static float potionsRegenValue;
	public static float potionsSpeedValue;
	public static float potionsStrengthValue;
	
	public static int potionsDamageResDur;
	public static int potionsFireResDur;
	public static int potionsHealDur;
	public static int potionsHungerDur;
	public static int potionsJumpDur;
	public static int potionsRegenDur;
	public static int potionsSpeedDur;
	public static int potionsStrengthDur;
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		defaultStamina = config.getFloat("defaultStamina", "Главное", 100.0F, 0.0F, 1000.0F, "!!ГРЯЗНЫМИ КОПЫТАМИ НЕ ТРОГАТЬ!! Максимальное значение стамины");
		regenerationValue = config.getFloat("regenerationValue", "Главное", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		waterRegenValue = config.getFloat("waterRegenValue", "Главное", 0.025F, 0.0F, 100.0F, "Реген в воде в тик");
		
		burnStaminaWhenHungry = config.getBoolean("burnStaminaWhenHungry", "Еда", true, "Обнулить стамину при голоде");
		highFoodLevel = config.getInt("highFoodLevel", "Еда", 12, 0, 20, "Уменьшить реген, если голода меньше");
		lowFoodLevel = config.getInt("lowFoodLevel", "Еда", 6, 0, 20, "Отключить реген, если голода меньше");
		lowFoodRegenValue = config.getFloat("lowFoodRegenValue", "Еда", 0.05F, 0.0F, 100.0F, "Реген в тик при низком голоде");
		
		flyEnableValue = config.getFloat("flyEnableValue", "Полёт", 5.0F, 0.0F, 100.0F, "Включать полёт при значении выше");
		flyDisableValue = config.getFloat("flyDisableValue", "Полёт", 0.01F, 0.0F, 100.0F, "Отключать полёт при значении ниже");
		flyHoverValue = config.getFloat("flyHoverValue", "Полёт", 0.015F, 0.0F, 100.0F, "!!НЕ ИСПОЛЬЗУЕТСЯ!! Уменьшение при зависании в воздухе");
		flySpendingValue = config.getFloat("flySpendingValue", "Полёт", 0.04F, 0.0F, 100.0F, "Уменьшение при обычном полёте");
		
		potionsDamageResValue = config.getFloat("potionsDamageResValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsresistance");
		potionsFireResValue = config.getFloat("potionsFireResValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsfireres");
		potionsHealValue = config.getFloat("potionsHealValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionshealth");
		potionsHungerValue = config.getFloat("potionsHungerValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionshunger");
		potionsJumpValue = config.getFloat("potionsJumpValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsjump");
		potionsRegenValue = config.getFloat("potionsRegenValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsregen");
		potionsSpeedValue = config.getFloat("potionsSpeedValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsspeed");
		potionsStrengthValue = config.getFloat("potionsStrengthValue", "Зельки", 30.0F, 0.0F, 100.0F, "Количество стамины за бафф potionsstrength");

		potionsDamageResDur = config.getInt("potionsDamageResDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsresistance");
		potionsFireResDur = config.getInt("potionsFireResDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsfireres");
		potionsHealDur = config.getInt("potionsHealDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionshealth");
		potionsHungerDur = config.getInt("potionsHungerDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionshunger");
		potionsJumpDur = config.getInt("potionsJumpDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsjump");
		potionsRegenDur = config.getInt("potionsRegenDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsregen");
		potionsSpeedDur = config.getInt("potionsSpeedDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsspeed");
		potionsStrengthDur = config.getInt("potionsStrengthDur", "Зельки", 20, 0, 120, "Продолжитьльность баффа в секундах potionsstrength");

		
		config.save();
	}
}
