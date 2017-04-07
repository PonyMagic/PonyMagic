package net.braunly.ponymagic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static float defaultStaminaPool;
	public static float defaultStaminaRegen;
	
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		defaultStaminaPool = config.getFloat("defaultStaminaPool", "Главное", 100.0F, 0.0F, 1000.0F, "Стандартное значение стамины");
		defaultStaminaRegen = config.getFloat("defaultStaminaRegen", "Главное", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		config.save();
	}
}
