package net.braunly.ponymagic.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("WeakerAccess")
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
	public static int highFoodLevel;
	public static int lowFoodLevel;

	// Fly
	public static Double flySpendingValue;
	public static float flyExhausting;
	
	// Metrics
	public static String graphiteHost;
	public static int graphitePort;
	public static String graphitePrefix;
	public static int reportInterval;
	public static boolean metricsEnabled;

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
		highFoodLevel = config.getInt("highFoodLevel", "Стамина", 12, 0, 20, "Уменьшить реген, если голода меньше");
		lowFoodLevel = config.getInt("lowFoodLevel", "Стамина", 6, 0, 20, "Отключить реген, если голода меньше");
		flySpendingValue = config
				.get("flySpendingValue", "Стамина", 0.08D, "Уменьшение при обычном полёте", 0.0D, 100.0D).getDouble();
		flyExhausting = config.getFloat("flyExhausting", "Стамина", 0.016F, 0.0F, 1.0F, "Потеря голода при полёте");

		graphiteHost = config.getString("graphiteHost", "metrics", "localhost", "Сервер Graphite");
		graphitePort = config.getInt("graphitePort", "metrics", 2003, 0, 65535, "Порт Graphite");
		graphitePrefix = config.getString("graphitePrefix", "metrics", "", "Префикс метрик");
		reportInterval = config.getInt("reportInterval", "metrics", 10, 1, 600, "Частота обновлений метрик");
		metricsEnabled = config.getBoolean("metricsEnabled", "metrics", false, "Включить отправку метрик?");

		// Passives
		highgroundDamage = config.getInt("highgroundDamage", "Passives", 10, 0, 100, "+% к урону от пассивки highground.");
		onedgeDamage = config.getInt("onedgeDamage", "Passives", 10, 0, 100, "+% к урону от пассивки onedge.");
		dodgingChance = config.getInt("dodgingChance", "Passives", 15, 0, 100, "Шанс в % уклонения с dodging.");
		
		// POTIONS
		potions.put("jump_boost#1",
				new Integer[] { config.getInt("jumpDur", "Potions", 60, 0, 600, "Длительность jump (сек)."),
						config.getInt("jumpStamina", "Potions", 30, 0, 100, "Стамина за jump."),
						config.getInt("jumpLvl", "Potions", 2, 1, 5, "Уровень эффекта jump.") });
		potions.put("antidote#1",
				new Integer[] { config.getInt("antidoteDur", "Potions", 60, 0, 600, "Длительность antidote. (сек)"),
						config.getInt("antidoteStamina", "Potions", 30, 0, 100, "Стамина за antidote."),
						config.getInt("antidoteLvl", "Potions", 1, 1, 5, "Уровень эффекта antidote.") });
		potions.put("climb#1",
				new Integer[] { config.getInt("climbDur", "Potions", 60, 0, 600, "Длительность climb. (сек)"),
						config.getInt("climbStamina", "Potions", 30, 0, 100, "Стамина за climb."),
						config.getInt("climbLvl", "Potions", 1, 1, 5, "Уровень эффекта climb.") });
		potions.put("dispel#1",
				new Integer[] { config.getInt("dispelDur", "Potions", 1, 0, 600, "Длительность dispel. (сек)"),
						config.getInt("dispelStamina", "Potions", 60, 0, 100, "Стамина за dispel."),
						config.getInt("dispelLvl", "Potions", 1, 1, 5, "Уровень эффекта dispel.") });
		potions.put("fire_resistance#1",
				new Integer[] {
						config.getInt("fireResistanceDur", "Potions", 90, 0, 600, "Длительность fireResistance. (сек)"),
						config.getInt("fireResistanceStamina", "Potions", 60, 0, 100, "Стамина за fireResistance."),
						config.getInt("fireResistanceLvl", "Potions", 1, 1, 5, "Уровень эффекта fireResistance.") });
		potions.put("drown#1",
				new Integer[] { config.getInt("drownDur", "Potions", 60, 0, 600, "Длительность drown. (сек)"),
						config.getInt("drownStamina", "Potions", 30, 0, 100, "Стамина за drown."),
						config.getInt("drownLvl", "Potions", 1, 1, 5, "Уровень эффекта drown.") });
		potions.put("slow#1",
				new Integer[] { config.getInt("slowDur", "Potions", 30, 0, 600, "Длительность slow. (сек)"),
						config.getInt("slowStamina", "Potions", 30, 0, 100, "Стамина за slow."),
						config.getInt("slowLvl", "Potions", 1, 1, 5, "Уровень эффекта slow.") });
		potions.put("purity#1",
				new Integer[] { config.getInt("purityDur", "Potions", 60, 0, 600, "Длительность purity. (сек)"),
						config.getInt("purityStamina", "Potions", 30, 0, 100, "Стамина за purity."),
						config.getInt("purityLvl", "Potions", 1, 1, 5, "Уровень эффекта purity.") });
		potions.put("night_vision#1",
				new Integer[] {
						config.getInt("nightVisionDur", "Potions", 60, 0, 600, "Длительность nightVision. (сек)"),
						config.getInt("nightVisionStamina", "Potions", 30, 0, 100, "Стамина за nightVision."),
						config.getInt("nightVisionLvl", "Potions", 1, 1, 5, "Уровень эффекта nightVision.") });
		potions.put("vulnerable#1",
				new Integer[] { config.getInt("vulnerableDur", "Potions", 30, 0, 600, "Длительность vulnerable. (сек)"),
						config.getInt("vulnerableStamina", "Potions", 30, 0, 100, "Стамина за vulnerable."),
						config.getInt("vulnerableLvl", "Potions", 1, 1, 5, "Уровень эффекта vulnerable.") });
		potions.put("step_up#1",
				new Integer[] { config.getInt("stepUpDur", "Potions", 60, 0, 600, "Длительность stepUp. (сек)"),
						config.getInt("stepUpStamina", "Potions", 30, 0, 100, "Стамина за stepUp."),
						config.getInt("stepUpLvl", "Potions", 2, 1, 5, "Уровень эффекта stepUp.") });
		potions.put("speed#1",
				new Integer[] {
						config.getInt("speedBoostDur1", "Potions", 30, 0, 600, "Длительность speedBoost. (сек)"),
						config.getInt("speedBoostStamina1", "Potions", 30, 0, 100, "Стамина за speedBoost."),
						config.getInt("speedBoostLvl1", "Potions", 2, 1, 5, "Уровень эффекта speedBoost.") });
		potions.put("speed#2",
				new Integer[] {
						config.getInt("speedBoostDur2", "Potions", 60, 0, 600,
								"Длительность speedBoost уровня 2. (сек)"),
						config.getInt("speedBoostStamina2", "Potions", 30, 0, 100, "Стамина за speedBoost уровня 2."),
						config.getInt("speedBoostLvl2", "Potions", 3, 1, 5, "Уровень эффекта speedBoost уровня 2.") });
		potions.put("strength#1",
				new Integer[] { config.getInt("strengthDur1", "Potions", 60, 0, 600, "Длительность strength. (сек)"),
						config.getInt("strengthStamina1", "Potions", 30, 0, 100, "Стамина за strength."),
						config.getInt("strengthLvl1", "Potions", 2, 1, 5, "Уровень эффекта strength.") });
		potions.put("haste#1",
				new Integer[] { config.getInt("hasteDur1", "Potions", 60, 0, 600, "Длительность haste. (сек)"),
						config.getInt("hasteStamina1", "Potions", 30, 0, 100, "Стамина за haste."),
						config.getInt("hasteLvl1", "Potions", 3, 1, 5, "Уровень эффекта haste.") });
		potions.put("regeneration#1",
				new Integer[] { config.getInt("hpRegenDur1", "Potions", 30, 0, 600, "Длительность hpRegen. (сек)"),
						config.getInt("hpRegenStamina1", "Potions", 30, 0, 100, "Стамина за hpRegen."),
						config.getInt("hpRegenLvl1", "Potions", 1, 1, 5, "Уровень эффекта hpRegen.") });
		potions.put("regeneration#2",
				new Integer[] {
						config.getInt("hpRegenDur2", "Potions", 45, 0, 600, "Длительность hpRegen уровня 2. (сек)"),
						config.getInt("hpRegenStamina2", "Potions", 30, 0, 100, "Стамина за hpRegen уровня 2."),
						config.getInt("hpRegenLvl2", "Potions", 2, 1, 5, "Уровень эффекта hpRege уровня 2n.") });
		potions.put("teleport_spawn#1",
				new Integer[] {
						config.getInt("teleportSpawnDur1", "Potions", 30, 0, 600, "Длительность teleportSpawn. (сек)"),
						config.getInt("teleportSpawnStamina1", "Potions", 60, 0, 100, "Стамина за teleportSpawn."),
						config.getInt("teleportSpawnLvl1", "Potions", 1, 1, 5, "Уровень эффекта teleportSpawn.") });
		potions.put("solid_core#1",
				new Integer[] { config.getInt("solidCoreDur1", "Potions", 60, 0, 600, "Длительность solidCore. (сек)"),
						config.getInt("solidCoreStamina1", "Potions", 30, 0, 100, "Стамина за solidCore."),
						config.getInt("solidCoreLvl1", "Potions", 1, 1, 5, "Уровень эффекта solidCore.") });
		potions.put("instant_health#1",
				new Integer[] { config.getInt("healDur1", "Potions", 3, 0, 600, "Длительность heal. (сек)"),
						config.getInt("healStamina1", "Potions", 30, 0, 100, "Стамина за heal."),
						config.getInt("healLvl1", "Potions", 1, 1, 5, "Уровень эффекта heal.") });
		potions.put("instant_health#2",
				new Integer[] {
						config.getInt("healDur2", "Potions", 6, 0, 600, "Длительность heal уровня 2. (сек)"),
						config.getInt("healStamina2", "Potions", 30, 0, 100, "Стамина за heal уровня 2."),
						config.getInt("healLvl2", "Potions", 1, 1, 5, "Уровень эффекта heal уровня 2.") });
		potions.put("slow_fall#1",
				new Integer[] { config.getInt("slowFallDur1", "Potions", 10, 0, 600, "Длительность slowFall. (сек)"),
						config.getInt("slowFallStamina1", "Potions", 30, 0, 100, "Стамина за slowFall."),
						config.getInt("slowFallLvl1", "Potions", 1, 1, 5, "Уровень эффекта slowFall.") });
		potions.put("slow_fall_auto#1",
				new Integer[] {
						config.getInt("slowFallAutoDur1", "Potions", 15, 0, 600, "Длительность slowFallAuto. (сек)"),
						config.getInt("slowFallAutoStamina1", "Potions", 0, 0, 100, "Стамина за slowFallAuto."),
						config.getInt("slowFallAutoLvl1", "Potions", 1, 1, 5, "Уровень эффекта slowFall.") });

		potions.put("shield#1",
				new Integer[] { config.getInt("shieldDur", "Spells", 60, 0, 600, "Длительность shield. (сек)"),
						config.getInt("shieldStamina", "Spells", 10, 0, 100, "Стамина за shield."),
						config.getInt("shieldStaminaPerDmg", "Spells", 3, 0, 100, "Стамина за единицу урона."), });
		potions.put("staminahealthregen#1",
				new Integer[] { config.getInt("staminahealthregenDur", "Spells", 20, 0, 20, "Длительность staminahealthregen. (сек)"),
						config.getInt("staminahealthregenStamina", "Spells", 0, 0, 100, "Стамина за staminahealthregen."),
						config.getInt("staminahealthregenLvl1", "Potions", 1, 1, 5, "Урон в секунду."), });

		// Spells
		spells.put("grow", new Integer[] { 
						config.getInt("growStamina", "Spells", 10, 0, 100, "Стамина за grow."), 
						config.getInt("growRadius", "Spells", 3, 0, 5, "Радиус grow."), });
		spells.put("unenchant",
				new Integer[] { 
						config.getInt("unenchantStamina", "Spells", 90, 0, 100, "Стамина за unenchant."), });
		spells.put("enchant",
				new Integer[] { 
						config.getInt("enchantStamina", "Spells", 90, 0, 100, "Стамина за enchant."),
						config.getInt("enchantLevel", "Spells", 30, 1, 60, "Уровень зачарования."),
						config.getInt("enchantExp", "Spells", 30, 0, 60, "Уровней опыта за зачарование."), });

		config.save();
	}
}
