package com.tmtravlr.potioncore;

import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;
import com.tmtravlr.potioncore.effects.PotionArchery;
import com.tmtravlr.potioncore.effects.PotionAttackDamageModified;
import com.tmtravlr.potioncore.effects.PotionExplosion;
import com.tmtravlr.potioncore.effects.PotionExplosionSelf;
import com.tmtravlr.potioncore.effects.PotionFire;
import com.tmtravlr.potioncore.effects.PotionKlutz;
import com.tmtravlr.potioncore.effects.PotionLaunch;
import com.tmtravlr.potioncore.effects.PotionLevitate;
import com.tmtravlr.potioncore.effects.PotionRecoil;
import com.tmtravlr.potioncore.effects.PotionRepair;
import com.tmtravlr.potioncore.effects.PotionRevival;
import com.tmtravlr.potioncore.effects.PotionRust;
import com.tmtravlr.potioncore.effects.PotionSlowfall;
import com.tmtravlr.potioncore.effects.PotionSpin;
import com.tmtravlr.potioncore.effects.PotionStepup;
import com.tmtravlr.potioncore.effects.PotionTeleport;
import com.tmtravlr.potioncore.effects.PotionTeleportSpawn;
import com.tmtravlr.potioncore.effects.PotionVulnerable;
import com.tmtravlr.potioncore.effects.PotionWeight;

import net.minecraftforge.common.config.Configuration;

public class ConfigLoader {

	public static Configuration config;
	
	public static boolean fixInvisibility;
	public static boolean fixBlindness;
	public static boolean modifyStrength;
	
	public static int maxPotions = 256;
	
	public static void load() {
		config.load();
		
		loadPotionConfigs();
		
		fixInvisibility = config.getBoolean("Fix Invisibiliby", "_options", true, "Fixes Invisibiliby so mobs can't see you as close while you are invisible.\nThey can see you at 1-12 blocks away depending on how much armor you\nhave on and if you are holding a item or not.");
		fixBlindness = config.getBoolean("Fix Blindness", "_options", true, "Fixes Blindness so mobs can't see things to attack unless they are really\nclose.");
		modifyStrength = config.getBoolean("Modify Strength", "_options", true, "Modifies the strength buff so it isn't as powerful (because it's\noverpowered; or you can make it more powerful).");
		PotionAttackDamageModified.modifierStrength = config.getFloat("Modified Strength Amount", "_options", PotionAttackDamageModified.modifierStrength, 0.0f, Float.POSITIVE_INFINITY, "Damage increase percent for the modified strength potion. Vanilla has 1.3.\n");
		maxPotions = config.getInt("Max Potions", "_options", maxPotions, 32, Integer.MAX_VALUE, "Change the maximum number of potions available in the game to this number.\n");
		
		for(String name : PotionCoreEffects.potionMap.keySet()) {
			PotionData data = PotionCoreEffects.potionMap.get(name);
			
			data.enabled = config.getBoolean("Enabled", name, data.enabled, "Is the " + name + " potion enabled?");
			data.id = config.getInt("Id", name, data.id, -1, maxPotions, "Id of the " + name + " potion. -1 makes it find the next available id.");
		}
		
		config.save();
	}

	/** Loads all the potion-specific config options. */
	private static void loadPotionConfigs() {
		PotionArchery.damageModifier = config.getFloat("Damage Modifier", PotionArchery.NAME, (float) PotionArchery.damageModifier, 0.0f, Float.POSITIVE_INFINITY,
				"The projectile damage modifier applied by the " + PotionArchery.NAME + " potion per each amplifier level.\n");

		PotionExplosion.explosionSize = config.getFloat("Explosion Size", PotionExplosion.NAME, PotionExplosion.explosionSize, 0.0f, Float.POSITIVE_INFINITY,
				"The base explosion size for the " + PotionExplosion.NAME + " potion.\n");

		PotionExplosionSelf.explosionSize = config.getFloat("Explosion Size", PotionExplosionSelf.NAME, PotionExplosionSelf.explosionSize, 0.0f, Float.POSITIVE_INFINITY,
				"The base explosion size for the " + PotionExplosionSelf.NAME + " potion.\n");

		PotionFire.fireDuration = config.getFloat("Fire Duration", PotionFire.NAME, PotionFire.fireDuration, 0.0f, Float.POSITIVE_INFINITY,
				"The fire duration in seconds applied by the " + PotionFire.NAME + " potion for each amplifier level.\n");

		PotionKlutz.damageModifier = config.getFloat("Damage Modifier", PotionKlutz.NAME, (float) PotionKlutz.damageModifier, Float.NEGATIVE_INFINITY, 0.0f,
				"The projectile damage modifier applied by the " + PotionKlutz.NAME + " potion per each amplifier level.\n");

		PotionLaunch.launchSpeed = config.getFloat("Launch Speed", PotionLaunch.NAME, (float) PotionLaunch.launchSpeed, 0.0f, Float.POSITIVE_INFINITY,
				"The initial launch speed for the " + PotionLaunch.NAME + " potion per each amplifier level.\n");

		PotionLevitate.floatSpeed = config.getFloat("Float Speed", PotionLevitate.NAME, (float) PotionLevitate.floatSpeed, 0.0f, Float.POSITIVE_INFINITY,
				"The float speed for the " + PotionLevitate.NAME + " potion per each amplifier level.\n");

		PotionRecoil.reflectDamage = config.getFloat("Damage Reflection", PotionRecoil.NAME, PotionRecoil.reflectDamage, 0.0f, 0.9f,
				"The percent of damage reflected for the " + PotionRecoil.NAME + " potion per each amplifier level.\n");

		PotionRepair.repairTime = config.getInt("Repair Time", PotionRepair.NAME, PotionRepair.repairTime, 0, Integer.MAX_VALUE,
				"The ticks between each repair from the " + PotionRepair.NAME + " potion.\n");

		PotionRevival.reviveHealth = config.getFloat("Revive Health", PotionRevival.NAME, PotionRevival.reviveHealth, 0.0f, Float.POSITIVE_INFINITY,
				"The amount of health recovered for each amplifier level when reviving from the " + PotionRevival.NAME + "\npotion per each amplifier level in half hearts.\n");

		PotionRust.damageTime = config.getInt("Damage Time", PotionRust.NAME, PotionRust.damageTime, 0, Integer.MAX_VALUE,
				"The ticks between each durability reduction from the " + PotionRust.NAME + " potion.\n");

		PotionSlowfall.maxSpeed = config.getFloat("Max Fall Speed", PotionSlowfall.NAME, (float) PotionSlowfall.maxSpeed, 0.0f, Float.POSITIVE_INFINITY,
				"The base maximum fall speed for the " + PotionSlowfall.NAME + " potion.\n");

		PotionSpin.rotationSpeed = config.getFloat("Rotation Speed", PotionSpin.NAME, PotionSpin.rotationSpeed, 0.0f, Float.POSITIVE_INFINITY,
				"The base maximum random rotation speed change for the " + PotionSpin.NAME + " potion.\n");

		PotionStepup.increase = config.getFloat("Step Height Increase", PotionStepup.NAME, PotionStepup.increase, 0.0f, Float.POSITIVE_INFINITY,
				"The step height increase per level of the " + PotionStepup.NAME + " potion.\n");

		PotionTeleport.teleportRange = config.getFloat("Teleport Radius", PotionTeleport.NAME, (float) PotionTeleport.teleportRange, 1.0f, Float.POSITIVE_INFINITY,
				"The base maximum teleport radius for the " + PotionTeleport.NAME + " potion.\n");

		PotionTeleportSpawn.teleportDelay = config.getInt("Teleport Delay", PotionTeleportSpawn.NAME, PotionTeleportSpawn.teleportDelay, 0, Integer.MAX_VALUE,
				"The number of ticks the player must stay still before teleporting with the " + PotionTeleportSpawn.NAME + "\npotion (with 20 ticks/second).\n");

		PotionVulnerable.damageMultiplier = config.getFloat("Damage Multiplier", PotionVulnerable.NAME, (float) PotionVulnerable.damageMultiplier, 0.0f, Float.POSITIVE_INFINITY,
				"The base damage multiplier for the " + PotionVulnerable.NAME + " potion.\n");

		PotionWeight.speedReduction = config.getFloat("Jump Height Reduction", PotionWeight.NAME, PotionWeight.speedReduction, 0.0f, Float.POSITIVE_INFINITY,
				"The base jump speed reduction for the " + PotionWeight.NAME + " potion.\nNormal jump speed is about 0.4 block/tick.\n");
	}
	
}
