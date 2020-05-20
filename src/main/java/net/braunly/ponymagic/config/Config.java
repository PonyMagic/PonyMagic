package net.braunly.ponymagic.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
	// Main
	public static int vanillaExpLvlForSkillReset;
	public static int raceExpPercentForSkillReset;
	public static int raceExpPercentForRaceChange;

	// Exp
	public static boolean expModifier;
	public static float expModifierAmount;
	public static int expPerLevel;

	// Stamina
	public static Double defaultStaminaPool;
	public static Double defaultStaminaRegen;
	public static Double lowFoodStaminaRegen;
	public static Double waterStaminaRegen;
	public static boolean burnStaminaWhenHungry;
	public static int lowFoodLevel;

	// Fly
	public static Double flySpendingValue;
	public static float flyExhausting;

	public static void load(File file) {
		Configuration config = new Configuration(file);
		config.load();

		// Main
		vanillaExpLvlForSkillReset = config.getInt("vanillaExpLvlForSkillReset", "Main", 30, 0, 250, "Количество уровней ванильного опыта для ресета скиллов (если нет книги).");
		raceExpPercentForSkillReset = config.getInt("raceExpPercentForSkillReset", "Main", 10, 0, 100, "Потеря опыта для ресета скиллов (в % от накопленного).");
		raceExpPercentForRaceChange = config.getInt("raceExpPercentForRaceChange", "Main", 10, 0, 100, "Потеря опыта при смене расы. (в % от накопленного)");

		// Опыт
		expModifier = config.getBoolean("expModifier", "Опыт", false, "Использовать модификатор опыта?");
		expModifierAmount = config.getFloat("expModifierAmount", "Опыт", 1.0F, 0.0F, 100.0F, "Модификатор опыта");
		expPerLevel = config.getInt("expPerLevel", "Опыт", 10000, 0, 100000, "Количество опыта на уровень");

		// Stamina
		defaultStaminaPool = config
				.get("defaultStaminaPool", "Стамина", 100.0D, "Стандартное значение стамины", 0.0D, 1000.0D)
				.getDouble();
		defaultStaminaRegen = config
				.get("defaultStaminaRegen", "Стамина", 0.15D, "Стандартный реген в тик", 0.0D, 100.0D).getDouble();
		waterStaminaRegen = config.get("waterStaminaRegen", "Стамина", 0.025D, "Реген в воде в тик", 0.0D, 100.0D)
				.getDouble();
		burnStaminaWhenHungry = config.getBoolean("burnStaminaWhenHungry", "Стамина", true,
				"Обнулить стамину при голоде");
		lowFoodStaminaRegen = config
				.get("lowFoodStaminaRegen", "Стамина", 0.05D, "Реген в тик при низком голоде", 0.0D, 100.0D)
				.getDouble();
		lowFoodLevel = config.getInt("lowFoodLevel", "Стамина", 8, 0, 20, "Уменьшить реген, если голода меньше");
		flySpendingValue = config
				.get("flySpendingValue", "Стамина", 0.08D, "Уменьшение при обычном полёте", 0.0D, 100.0D).getDouble();
		flyExhausting = config.getFloat("flyExhausting", "Стамина", 0.016F, 0.0F, 1.0F, "Потеря голода при полёте");

		config.save();
	}
}
