package net.braunly.ponymagic.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {

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

	// Passives
	public static int highgroundDamage;
	public static int onedgeDamage;
	public static int dodgingChance;
	
	// Spells
	public static Map<String, Integer[]> potions = new HashMap<>();
	public static Map<String, Integer[]> spells = new HashMap<>();

	public static void load(File file) {
		Configuration config = new Configuration(file);
		config.load();

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

		// Passives
		highgroundDamage = config.getInt("highgroundDamage", "Passives", 10, 0, 100, "+% к урону от пассивки highground.");
		onedgeDamage = config.getInt("onedgeDamage", "Passives", 20, 0, 100, "+% к урону от пассивки onedge.");
		dodgingChance = config.getInt("dodgingChance", "Passives", 15, 0, 100, "Шанс в % уклонения с dodging.");
		
		// POTIONS
		potions.put("jump_boost#1",
				new Integer[] { config.getInt("jumpDur", "Potions", 1200, 0, 12000, "Длительность jump (в тиках)."),
						config.getInt("jumpStamina", "Potions", 30, 0, 100, "Стамина за jump."),
						config.getInt("jumpLvl", "Potions", 2, 1, 5, "Уровень эффекта jump.") });
		potions.put("antidote#1",
				new Integer[] { config.getInt("antidoteDur", "Potions", 1200, 0, 12000, "Длительность antidote. (в тиках)"),
						config.getInt("antidoteStamina", "Potions", 45, 0, 100, "Стамина за antidote."),
						config.getInt("antidoteLvl", "Potions", 1, 1, 5, "Уровень эффекта antidote.") });
		potions.put("climb#1",
				new Integer[] { config.getInt("climbDur", "Potions", 1200, 0, 12000, "Длительность climb. (в тиках)"),
						config.getInt("climbStamina", "Potions", 30, 0, 100, "Стамина за climb."),
						config.getInt("climbLvl", "Potions", 1, 1, 5, "Уровень эффекта climb.") });
		potions.put("dispel#1",
				new Integer[] { config.getInt("dispelDur", "Potions", 1, 0, 12000, "Длительность dispel. (в тиках)"),
						config.getInt("dispelStamina", "Potions", 60, 0, 100, "Стамина за dispel."),
						config.getInt("dispelLvl", "Potions", 1, 1, 5, "Уровень эффекта dispel.") });
		potions.put("fire_resistance#1",
				new Integer[] {
						config.getInt("fireResistanceDur", "Potions", 1800, 0, 12000, "Длительность fireResistance. (в тиках)"),
						config.getInt("fireResistanceStamina", "Potions", 60, 0, 100, "Стамина за fireResistance."),
						config.getInt("fireResistanceLvl", "Potions", 1, 1, 5, "Уровень эффекта fireResistance.") });
		potions.put("resistance#1",
				new Integer[] {
						config.getInt("resistanceDur", "Potions", 1200, 0, 12000, "Длительность resistance. (в тиках)"),
						config.getInt("resistanceStamina", "Potions", 60, 0, 100, "Стамина за resistance."),
						config.getInt("resistanceLvl", "Potions", 1, 1, 5, "Уровень эффекта resistance.") });
		potions.put("drown#1",
				new Integer[] { config.getInt("drownDur", "Potions", 1200, 0, 12000, "Длительность drown. (в тиках)"),
						config.getInt("drownStamina", "Potions", 30, 0, 100, "Стамина за drown."),
						config.getInt("drownLvl", "Potions", 1, 1, 5, "Уровень эффекта drown.") });
		potions.put("slowness#1",
				new Integer[] { config.getInt("slownessDur", "Potions", 600, 0, 12000, "Длительность slowness. (в тиках)"),
						config.getInt("slownessStamina", "Potions", 30, 0, 100, "Стамина за slowness."),
						config.getInt("slownessLvl", "Potions", 1, 1, 5, "Уровень эффекта slowness.") });
		potions.put("purity#1",
				new Integer[] { config.getInt("purityDur", "Potions", 1200, 0, 12000, "Длительность purity. (в тиках)"),
						config.getInt("purityStamina", "Potions", 45, 0, 100, "Стамина за purity."),
						config.getInt("purityLvl", "Potions", 1, 1, 5, "Уровень эффекта purity.") });
		potions.put("night_vision#1",
				new Integer[] {
						config.getInt("nightVisionDur", "Potions", 1200, 0, 12000, "Длительность nightVision. (в тиках)"),
						config.getInt("nightVisionStamina", "Potions", 30, 0, 100, "Стамина за nightVision."),
						config.getInt("nightVisionLvl", "Potions", 1, 1, 5, "Уровень эффекта nightVision.") });
		potions.put("vulnerable#1",
				new Integer[] { config.getInt("vulnerableDur", "Potions", 600, 0, 12000, "Длительность vulnerable. (в тиках)"),
						config.getInt("vulnerableStamina", "Potions", 30, 0, 100, "Стамина за vulnerable."),
						config.getInt("vulnerableLvl", "Potions", 1, 1, 5, "Уровень эффекта vulnerable.") });
		potions.put("step_up#1",
				new Integer[] { config.getInt("stepUpDur", "Potions", 1200, 0, 12000, "Длительность stepUp. (в тиках)"),
						config.getInt("stepUpStamina", "Potions", 30, 0, 100, "Стамина за stepUp."),
						config.getInt("stepUpLvl", "Potions", 2, 1, 5, "Уровень эффекта stepUp.") });
		potions.put("speed#1",
				new Integer[] {
						config.getInt("speedBoostDur1", "Potions", 600, 0, 12000, "Длительность speedBoost. (в тиках)"),
						config.getInt("speedBoostStamina1", "Potions", 30, 0, 100, "Стамина за speedBoost."),
						config.getInt("speedBoostLvl1", "Potions", 2, 1, 5, "Уровень эффекта speedBoost.") });
		potions.put("speed#2",
				new Integer[] {
						config.getInt("speedBoostDur2", "Potions", 1200, 0, 12000,
								"Длительность speedBoost уровня 2. (в тиках)"),
						config.getInt("speedBoostStamina2", "Potions", 30, 0, 100, "Стамина за speedBoost уровня 2."),
						config.getInt("speedBoostLvl2", "Potions", 3, 1, 5, "Уровень эффекта speedBoost уровня 2.") });
		potions.put("strength#1",
				new Integer[] { config.getInt("strengthDur1", "Potions", 1200, 0, 12000, "Длительность strength. (в тиках)"),
						config.getInt("strengthStamina1", "Potions", 30, 0, 100, "Стамина за strength."),
						config.getInt("strengthLvl1", "Potions", 2, 1, 5, "Уровень эффекта strength.") });
		potions.put("haste#1",
				new Integer[] { config.getInt("hasteDur1", "Potions", 600, 0, 12000, "Длительность haste. (в тиках)"),
						config.getInt("hasteStamina1", "Potions", 50, 0, 100, "Стамина за haste."),
						config.getInt("hasteLvl1", "Potions", 3, 1, 5, "Уровень эффекта haste.") });
		potions.put("regeneration#1",
				new Integer[] { config.getInt("hpRegenDur1", "Potions", 600, 0, 12000, "Длительность hpRegen. (в тиках)"),
						config.getInt("hpRegenStamina1", "Potions", 30, 0, 100, "Стамина за hpRegen."),
						config.getInt("hpRegenLvl1", "Potions", 1, 1, 5, "Уровень эффекта hpRegen.") });
		potions.put("regeneration#2",
				new Integer[] {
						config.getInt("hpRegenDur2", "Potions", 900, 0, 12000, "Длительность hpRegen уровня 2. (в тиках)"),
						config.getInt("hpRegenStamina2", "Potions", 50, 0, 100, "Стамина за hpRegen уровня 2."),
						config.getInt("hpRegenLvl2", "Potions", 2, 1, 5, "Уровень эффекта hpRege уровня 2n.") });
		potions.put("teleport_spawn#1",
				new Integer[] {
						config.getInt("teleportSpawnDur1", "Potions", 600, 0, 12000, "Длительность teleportSpawn. (в тиках)"),
						config.getInt("teleportSpawnStamina1", "Potions", 60, 0, 100, "Стамина за teleportSpawn."),
						config.getInt("teleportSpawnLvl1", "Potions", 1, 1, 5, "Уровень эффекта teleportSpawn.") });
		potions.put("solid_core#1",
				new Integer[] { config.getInt("solidCoreDur1", "Potions", 1200, 0, 12000, "Длительность solidCore. (в тиках)"),
						config.getInt("solidCoreStamina1", "Potions", 30, 0, 100, "Стамина за solidCore."),
						config.getInt("solidCoreLvl1", "Potions", 1, 1, 5, "Уровень эффекта solidCore.") });
		potions.put("instant_health#1",
				new Integer[] { config.getInt("healDur1", "Potions", 20, 0, 12000, "Длительность heal. (в тиках)"),
						config.getInt("healStamina1", "Potions", 30, 0, 100, "Стамина за heal."),
						config.getInt("healLvl1", "Potions", 1, 1, 5, "Уровень эффекта heal.") });
		potions.put("instant_health#2",
				new Integer[] {
						config.getInt("healDur2", "Potions", 60, 0, 12000, "Длительность heal уровня 2. (в тиках)"),
						config.getInt("healStamina2", "Potions", 30, 0, 100, "Стамина за heal уровня 2."),
						config.getInt("healLvl2", "Potions", 1, 1, 5, "Уровень эффекта heal уровня 2.") });
		potions.put("slow_fall#1",
				new Integer[] { config.getInt("slowFallDur1", "Potions", 200, 0, 12000, "Длительность slowFall. (в тиках)"),
						config.getInt("slowFallStamina1", "Potions", 30, 0, 100, "Стамина за slowFall."),
						config.getInt("slowFallLvl1", "Potions", 1, 1, 5, "Уровень эффекта slowFall.") });
		potions.put("slow_fall_auto#1",
				new Integer[] {
						config.getInt("slowFallAutoDur1", "Potions", 300, 0, 12000, "Длительность slowFallAuto. (в тиках)"),
						config.getInt("slowFallAutoStamina1", "Potions", 0, 0, 100, "Стамина за slowFallAuto."),
						config.getInt("slowFallAutoLvl1", "Potions", 1, 1, 5, "Уровень эффекта slowFall.") });
		potions.put("archery#1",
				new Integer[] { config.getInt("archeryDur1", "Potions", 200, 0, 12000, "Длительность archery. (в тиках)"),
						config.getInt("archeryStamina1", "Potions", 45, 0, 100, "Стамина за archery."),
						config.getInt("archeryLvl1", "Potions", 1, 1, 5, "Уровень эффекта archery.") });
		potions.put("recoil#1",
				new Integer[] { config.getInt("recoilDur1", "Potions", 300, 0, 12000, "Длительность recoil. (в тиках)"),
						config.getInt("recoilStamina1", "Potions", 40, 0, 100, "Стамина за recoil."),
						config.getInt("recoilLvl1", "Potions", 1, 1, 5, "Уровень эффекта recoil.") });
		potions.put("burst#1",
				new Integer[] { config.getInt("burstDur1", "Potions", 1, 0, 12000, "Длительность burst. (в тиках)"),
						config.getInt("burstStamina1", "Potions", 30, 0, 100, "Стамина за burst."),
						config.getInt("burstLvl1", "Potions", 0, 0, 5, "Уровень эффекта burst.") });
		potions.put("repair#1",
				new Integer[] { config.getInt("repairDur1", "Potions", 1400, 0, 12000, "Длительность repair. (в тиках)"),
						config.getInt("repairStamina1", "Potions", 70, 0, 100, "Стамина за repair."),
						config.getInt("repairLvl1", "Potions", 9, 1, 5, "Уровень эффекта repair.") });
		potions.put("revival#1",
				new Integer[] { config.getInt("revivalDur1", "Potions", 100, 0, 12000, "Длительность revival. (в тиках)"),
						config.getInt("revivalStamina1", "Potions", 90, 0, 100, "Стамина за revival."),
						config.getInt("revivalLvl1", "Potions", 0, 0, 5, "Уровень эффекта revival.") });
		potions.put("teleport_surface#1",
				new Integer[] { config.getInt("teleport_surfaceDur1", "Potions", 1, 0, 12000, "Длительность tpsurface. (в тиках)"),
						config.getInt("teleport_surfaceStamina1", "Potions", 30, 0, 100, "Стамина за tpsurface."),
						config.getInt("teleport_surfaceLvl1", "Potions", 0, 0, 5, "Уровень эффекта tpsurface.") });
		potions.put("flight#1",
				new Integer[] { config.getInt("flightDur1", "Potions", 100, 0, 12000, "Длительность fly. (в тиках)"),
						config.getInt("flightStamina1", "Potions", 60, 0, 100, "Стамина за fly."),
						config.getInt("flightLvl1", "Potions", 1, 1, 5, "Уровень эффекта fly.") });
		potions.put("invisibility#1",
				new Integer[] { config.getInt("invisibilityDur1", "Potions", 600, 0, 12000, "Длительность invisibility. (в тиках)"),
						config.getInt("invisibilityStamina1", "Potions", 30, 0, 100, "Стамина за invisibility."),
						config.getInt("invisibilityLvl1", "Potions", 1, 1, 5, "Уровень эффекта invisibility.") });
		potions.put("cure#1",
				new Integer[] { config.getInt("cureDur1", "Potions", 1, 0, 12000, "Длительность cure. (в тиках)"),
						config.getInt("cureStamina1", "Potions", 60, 0, 100, "Стамина за cure."),
						config.getInt("cureLvl1", "Potions", 1, 1, 5, "Уровень эффекта cure.") });
		potions.put("extension#1",
				new Integer[] { config.getInt("extensionDur1", "Potions", 600, 0, 12000, "Длительность extension. (в тиках)"),
						config.getInt("extensionStamina1", "Potions", 45, 0, 100, "Стамина за extension."),
						config.getInt("extensionLvl1", "Potions", 1, 1, 5, "Уровень эффекта extension.") });
		potions.put("invert#1",
				new Integer[] { config.getInt("invertDur1", "Potions", 1, 0, 12000, "Длительность invert. (в тиках)"),
						config.getInt("invertStamina1", "Potions", 30, 0, 100, "Стамина за invert."),
						config.getInt("invertLvl1", "Potions", 1, 1, 5, "Уровень эффекта invert`.") });

		potions.put("shield#1",
				new Integer[] { config.getInt("shieldDur", "Spells", 1200, 0, 12000, "Длительность shield. (в тиках)"),
						config.getInt("shieldStamina", "Spells", 10, 0, 100, "Стамина за shield."),
						config.getInt("shieldStaminaPerDmg", "Spells", 3, 0, 100, "Стамина за единицу урона."), });
		potions.put("staminahealthregen#1",
				new Integer[] { config.getInt("staminahealthregenDur", "Spells", 400, 0, 12000, "Длительность staminahealthregen. (в тиках)"),
						config.getInt("staminahealthregenStamina", "Spells", 0, 0, 100, "Стамина за staminahealthregen."),
						config.getInt("staminahealthregenLvl1", "Spells", 1, 1, 5, "Урон в секунду."), });

		// Spells
		spells.put("grow", new Integer[] { 
						config.getInt("growStamina", "Spells", 10, 0, 100, "Стамина за grow."), 
						config.getInt("growRadius", "Spells", 3, 0, 5, "Радиус grow."), });
		spells.put("unenchant",
				new Integer[] { 
						config.getInt("unenchantStamina", "Spells", 45, 0, 100, "Стамина за unenchant."), });
		spells.put("enchant",
				new Integer[] { 
						config.getInt("enchantStamina", "Spells", 90, 0, 100, "Стамина за enchant."),
						config.getInt("enchantLevel", "Spells", 30, 1, 60, "Уровень зачарования."),
						config.getInt("enchantExp", "Spells", 30, 0, 60, "Уровней опыта за зачарование."), });

		config.save();
	}
}
