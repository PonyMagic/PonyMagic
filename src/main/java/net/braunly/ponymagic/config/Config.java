package net.braunly.ponymagic.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean expModifier;
	public static float expModifierAmount;
	public static int expPerLevel;
	
	public static float defaultStaminaPool;
	public static float defaultStaminaRegen;
	public static float lowFoodStaminaRegen;
	public static float waterStaminaRegen;
	public static boolean burnStaminaWhenHungry;
	public static int highFoodLevel;
	public static int lowFoodLevel;
	
	public static float flySpendingValue;
	
	public static Map<String, Integer[]> potions = new HashMap<String, Integer[]>();
	public static Map<String, Integer[]> spells = new HashMap<String, Integer[]>();
	
	
	public static void load(File file){
		Configuration config = new Configuration(file);
		config.load();
		
		// Опыт
		expModifier = config.getBoolean("expModifier", "Опыт", false, "Использовать модификатор опыта?");
		expModifierAmount = config.getFloat("expModifierAmount", "Опыт", 1.0F, 0.0F, 100.0F, "Модификатор опыта");
		expPerLevel = config.getInt("expPerLevel", "Опыт", 10000, 0, 100000, "Количество опыта на уровень");
		
		// Stamina
		defaultStaminaPool = config.getFloat("defaultStaminaPool", "Стамина", 100.0F, 0.0F, 1000.0F, "Стандартное значение стамины");
		defaultStaminaRegen = config.getFloat("defaultStaminaRegen", "Стамина", 0.15F, 0.0F, 100.0F, "Стандартный реген в тик");
		waterStaminaRegen = config.getFloat("waterStaminaRegen", "Стамина", 0.025F, 0.0F, 100.0F, "Реген в воде в тик");
		burnStaminaWhenHungry = config.getBoolean("burnStaminaWhenHungry", "Стамина", true, "Обнулить стамину при голоде");
		lowFoodStaminaRegen = config.getFloat("lowFoodStaminaRegen", "Стамина", 0.05F, 0.0F, 100.0F, "Реген в тик при низком голоде");
		highFoodLevel = config.getInt("highFoodLevel", "Стамина", 12, 0, 20, "Уменьшить реген, если голода меньше");
		lowFoodLevel = config.getInt("lowFoodLevel", "Стамина", 6, 0, 20, "Отключить реген, если голода меньше");
		flySpendingValue = config.getFloat("flySpendingValue", "Стамина", 0.04F, 0.0F, 100.0F, "Уменьшение при обычном полёте");
		
		// POTIONS
		potions.put("jump#1", new Integer[] {
				config.getInt("jumpDur", "Potions", 60, 0, 600, "Длительность jump (сек)."),
				config.getInt("jumpStamina", "Potions", 30, 0, 100, "Стамина за jump."),
				config.getInt("jumpLvl", "Potions", 2, 1, 5, "Уровень эффекта jump.")
		});
		potions.put("antidote#1", new Integer[] {
				config.getInt("antidoteDur", "Potions", 60, 0, 600, "Длительность antidote. (сек)"),
				config.getInt("antidoteStamina", "Potions", 30, 0, 100, "Стамина за antidote."),
				config.getInt("antidoteLvl", "Potions", 1, 1, 5, "Уровень эффекта antidote.")
		});
		potions.put("climb#1", new Integer[] {
				config.getInt("climbDur", "Potions", 60, 0, 600, "Длительность climb. (сек)"),
				config.getInt("climbStamina", "Potions", 30, 0, 100, "Стамина за climb."),
				config.getInt("climbLvl", "Potions", 1, 1, 5, "Уровень эффекта climb.")
		});
		potions.put("dispel#1", new Integer[] {
				config.getInt("dispelDur", "Potions", 1, 0, 600, "Длительность dispel. (сек)"),
				config.getInt("dispelStamina", "Potions", 60, 0, 100, "Стамина за dispel."),
				config.getInt("dispelLvl", "Potions", 1, 1, 5, "Уровень эффекта dispel.")
		});
		potions.put("fireResistance#1", new Integer[] {
				config.getInt("fireResistanceDur", "Potions", 90, 0, 600, "Длительность fireResistance. (сек)"),
				config.getInt("fireResistanceStamina", "Potions", 60, 0, 100, "Стамина за fireResistance."),
				config.getInt("fireResistanceLvl", "Potions", 1, 1, 5, "Уровень эффекта fireResistance.")
		});
		potions.put("drown#1", new Integer[] {
				config.getInt("drownDur", "Potions", 60, 0, 600, "Длительность drown. (сек)"),
				config.getInt("drownStamina", "Potions", 30, 0, 100, "Стамина за drown."),
				config.getInt("drownLvl", "Potions", 1, 1, 5, "Уровень эффекта drown.")
		});
		potions.put("slow#1", new Integer[] {
				config.getInt("slowDur", "Potions", 30, 0, 600, "Длительность slow. (сек)"),
				config.getInt("slowStamina", "Potions", 30, 0, 100, "Стамина за slow."),
				config.getInt("slowLvl", "Potions", 1, 1, 5, "Уровень эффекта slow.")
		});
		potions.put("purity#1", new Integer[] {
				config.getInt("purityDur", "Potions", 60, 0, 600, "Длительность purity. (сек)"),
				config.getInt("purityStamina", "Potions", 30, 0, 100, "Стамина за purity."),
				config.getInt("purityLvl", "Potions", 1, 1, 5, "Уровень эффекта purity.")
		});
		potions.put("nightVision#1", new Integer[] {
				config.getInt("nightVisionDur", "Potions", 60, 0, 600, "Длительность nightVision. (сек)"),
				config.getInt("nightVisionStamina", "Potions", 30, 0, 100, "Стамина за nightVision."),
				config.getInt("nightVisionLvl", "Potions", 1, 1, 5, "Уровень эффекта nightVision.")
		});
		potions.put("vulnerable#1", new Integer[] {
				config.getInt("vulnerableDur", "Potions", 30, 0, 600, "Длительность vulnerable. (сек)"),
				config.getInt("vulnerableStamina", "Potions", 30, 0, 100, "Стамина за vulnerable."),
				config.getInt("vulnerableLvl", "Potions", 1, 1, 5, "Уровень эффекта vulnerable.")
		});
		potions.put("stepUp#1", new Integer[] {
				config.getInt("stepUpDur", "Potions", 60, 0, 600, "Длительность stepUp. (сек)"),
				config.getInt("stepUpStamina", "Potions", 30, 0, 100, "Стамина за stepUp."),
				config.getInt("stepUpLvl", "Potions", 2, 1, 5, "Уровень эффекта stepUp.")
		});
		potions.put("speedBoost#1", new Integer[] {
				config.getInt("speedBoostDur1", "Potions", 30, 0, 600, "Длительность speedBoost. (сек)"),
				config.getInt("speedBoostStamina1", "Potions", 30, 0, 100, "Стамина за speedBoost."),
				config.getInt("speedBoostLvl1", "Potions", 2, 1, 5, "Уровень эффекта speedBoost.")
		});
		potions.put("speedBoost#2", new Integer[] {
				config.getInt("speedBoostDur2", "Potions", 60, 0, 600, "Длительность speedBoost уровня 2. (сек)"),
				config.getInt("speedBoostStamina2", "Potions", 30, 0, 100, "Стамина за speedBoost уровня 2."),
				config.getInt("speedBoostLvl2", "Potions", 3, 1, 5, "Уровень эффекта speedBoost уровня 2.")
		});
		potions.put("strength#1", new Integer[] {
				config.getInt("strengthDur1", "Potions", 60, 0, 600, "Длительность strength. (сек)"),
				config.getInt("strengthStamina1", "Potions", 30, 0, 100, "Стамина за strength."),
				config.getInt("strengthLvl1", "Potions", 2, 1, 5, "Уровень эффекта strength.")
		});
		potions.put("haste#1", new Integer[] {
				config.getInt("hasteDur1", "Potions", 60, 0, 600, "Длительность haste. (сек)"),
				config.getInt("hasteStamina1", "Potions", 30, 0, 100, "Стамина за haste."),
				config.getInt("hasteLvl1", "Potions", 3, 1, 5, "Уровень эффекта haste.")
		});
		potions.put("hpRegen#1", new Integer[] {
				config.getInt("hpRegenDur1", "Potions", 30, 0, 600, "Длительность hpRegen. (сек)"),
				config.getInt("hpRegenStamina1", "Potions", 30, 0, 100, "Стамина за hpRegen."),
				config.getInt("hpRegenLvl1", "Potions", 1, 1, 5, "Уровень эффекта hpRegen.")
		});
		potions.put("hpRegen#2", new Integer[] {
				config.getInt("hpRegenDur2", "Potions", 45, 0, 600, "Длительность hpRegen уровня 2. (сек)"),
				config.getInt("hpRegenStamina2", "Potions", 30, 0, 100, "Стамина за hpRegen уровня 2."),
				config.getInt("hpRegenLvl2", "Potions", 2, 1, 5, "Уровень эффекта hpRege уровня 2n.")
		});
		potions.put("teleportSpawn#1", new Integer[] {
				config.getInt("teleportSpawnDur1", "Potions", 30, 0, 600, "Длительность teleportSpawn. (сек)"),
				config.getInt("teleportSpawnStamina1", "Potions", 60, 0, 100, "Стамина за teleportSpawn."),
				config.getInt("teleportSpawnLvl1", "Potions", 1, 1, 5, "Уровень эффекта teleportSpawn.")
		});
		potions.put("solidCore#1", new Integer[] {
				config.getInt("solidCoreDur1", "Potions", 60, 0, 600, "Длительность solidCore. (сек)"),
				config.getInt("solidCoreStamina1", "Potions", 30, 0, 100, "Стамина за solidCore."),
				config.getInt("solidCoreLvl1", "Potions", 1, 1, 5, "Уровень эффекта solidCore.")
		});
		potions.put("heal#1", new Integer[] {
				config.getInt("hpRegenDur1", "Potions", 10, 0, 600, "Длительность heal. (сек)"),
				config.getInt("hpRegenStamina1", "Potions", 30, 0, 100, "Стамина за heal."),
				config.getInt("hpRegenLvl1", "Potions", 1, 1, 5, "Уровень эффекта heal.")
		});
		potions.put("heal#2", new Integer[] {
				config.getInt("hpRegenDur2", "Potions", 20, 0, 600, "Длительность heal уровня 2. (сек)"),
				config.getInt("hpRegenStamina2", "Potions", 30, 0, 100, "Стамина за heal уровня 2."),
				config.getInt("hpRegenLvl2", "Potions", 1, 1, 5, "Уровень эффекта heal уровня 2.")
		});
		potions.put("slowFall#1", new Integer[] {
				config.getInt("slowFallDur1", "Potions", 10, 0, 600, "Длительность slowFall. (сек)"),
				config.getInt("slowFallStamina1", "Potions", 30, 0, 100, "Стамина за slowFall."),
				config.getInt("slowFallLvl1", "Potions", 1, 1, 5, "Уровень эффекта slowFall.")
		});
		
		// Spells
		spells.put("grow", new Integer[] {
				config.getInt("growStamina", "Spells", 10, 0, 100, "Стамина за grow."),
		});
		spells.put("unenchant", new Integer[] {
				config.getInt("unenchantStamina", "Spells", 90, 0, 100, "Стамина за unenchant."),
		});
		spells.put("enchant", new Integer[] {
				config.getInt("enchantStamina", "Spells", 90, 0, 100, "Стамина за enchant."),
		});
		spells.put("shield", new Integer[] {
				config.getInt("shieldDur", "Spells", 60, 0, 600, "Длительность shield. (сек)"),
				config.getInt("shieldStamina", "Spells", 10, 0, 100, "Стамина за shield."),
				config.getInt("shieldStaminaPerDmg", "Spells", 1, 0, 100, "Стамина за единицу урона."),
		});
		
		config.save();
	}
}
