package net.braunly.ponymagic.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
	// Main
	public static int vanillaExpLvlForSkillReset;
	public static int removeLevelsForRaceChange;

	// Stamina
	public static Double defaultStaminaPool;
	public static Double defaultStaminaRegen;
	public static Double lowFoodStaminaRegen;
	public static Double waterStaminaRegen;
	public static int lowFoodLevel;

	// Fly
	public static Double flySpendingValue;

	// FIXME: rewrite configuration
	public Config(File configFile) {
		Configuration config = new Configuration(configFile);
		config.load();

		// Main
		vanillaExpLvlForSkillReset = config.getInt(
				"vanillaExpLvlForSkillReset",
				"Main",
				30,
				0,
				250,
				"Remove vanilla exp levels for skills reset"
		);
		removeLevelsForRaceChange = config.getInt(
				"removeLevelsForRaceChange",
				"Main",
				3,
				0,
				30,
				"Remove levels for race change"
		);

		// Stamina
		defaultStaminaPool = config.get(
				"Stamina",
				"defaultStaminaPool",
				100.0D,
				"Default stamina",
				0.0D,
				1000.0D
		).getDouble();
		defaultStaminaRegen = config.get(
				"Stamina",
				"defaultStaminaRegen",
				0.15D,
				"Normal stamina regen level",
				0.0D,
				100.0D
		).getDouble();
		waterStaminaRegen = config.get(
				"Stamina",
				"waterStaminaRegen",
				0.025D,
				"Stamina regen level when swimming in water",
				0.0D,
				100.0D).getDouble();
		lowFoodStaminaRegen = config.get(
				"Stamina",
				"lowFoodStaminaRegen",
				0.05D,
				"Low-food stamina regen",
				0.0D,
				100.0D
		).getDouble();
		lowFoodLevel = config.getInt(
				"lowFoodLevel",
				"Stamina",
				8,
				0,
				20,
				"Low-food level for stamina regen"
		);
		flySpendingValue = config.get(
				"Stamina",
				"flySpendingValue",
				0.08D,
				"Stamina for flying",
				0.0D,
				100.0D
		).getDouble();

		config.save();
	}
}
