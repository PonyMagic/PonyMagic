package net.braunly.ponymagic.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.braunly.ponymagic.PonyMagic;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Config {
	private static String configPath = null;
	private static final Map<String, String> configMap = new HashMap<>();

	private Config() {
		throw new IllegalStateException("Utility class");
	}

	public static void init(File modConfigDir) {
		configPath = modConfigDir.getAbsolutePath() + "/" + PonyMagic.MODID + "/main.json";
		load();
	}


	public static void load() {
		if (configPath == null) {
			PonyMagic.log.error("Main config not initialized!");
			return;
		}

		PonyMagic.log.info("Loading main config file");
		File configFile = new File(configPath);
		if (!configFile.exists()) {
			// Copy default config
			makeDefaultConfig(configFile);
		}

		Gson gson = new Gson();
		JsonObject jsonMap = null;

		try {
			// Load config file
			jsonMap = gson.fromJson(new FileReader(configFile), JsonObject.class);
		} catch (FileNotFoundException exception) {
			// already checked above
			PonyMagic.log.catching(exception);
		}
		Type typeToken = new TypeToken<HashMap<String, String>>(){}.getType();
		HashMap<String, String> loadedMap = gson.fromJson(jsonMap, typeToken);
		if (loadedMap != null) {
			configMap.putAll(loadedMap);
		} else {
			PonyMagic.log.error("Main config format error!");
		}
	}

	public static int getRemoveLevelsForRaceChange() {
		return Integer.parseInt(configMap.getOrDefault("remove_levels_for_race_change", "3"));
	}
	public static int getVanillaExpLvlForSkillReset() {
		return Integer.parseInt(configMap.getOrDefault("vanilla_exp_for_skill_reset", "30"));
	}
	public static Double getFlySpendingValue() {
		return Double.parseDouble(configMap.getOrDefault("stamina_for_fly_per_tick", "0.08"));
	}
	public static Double getDefaultStaminaRegen() {
		return Double.parseDouble(configMap.getOrDefault("default_stamina_regen_per_tick", "0.15"));
	}
	public static Double getLowFoodStaminaRegen() {
		return Double.parseDouble(configMap.getOrDefault("low_food_stamina_regen", "0.05"));
	}
	public static Double getWaterStaminaRegen() {
		return Double.parseDouble(configMap.getOrDefault("water_stamina_regen_per_tick", "0.025"));
	}
	public static int getLowFoodLevel() {
		return Integer.parseInt(configMap.getOrDefault("low_food_level", "8"));
	}

	private static void makeDefaultConfig(File configFile) {
		try {
			String defaultConfigPath = "/assets/" + PonyMagic.MODID + "/config/main.json";
			InputStream inputStream = LevelConfig.class.getResourceAsStream(defaultConfigPath);
			FileUtils.copyInputStreamToFile(inputStream, configFile);
			PonyMagic.log.info("Created default main config {}", configFile.getName());
		} catch (IOException exception) {
			PonyMagic.log.catching(exception);
		}
	}
}
