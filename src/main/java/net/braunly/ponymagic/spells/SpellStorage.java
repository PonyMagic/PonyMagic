package net.braunly.ponymagic.spells;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.braunly.ponymagic.spells.potion.SpellPotionSplash;
import net.braunly.ponymagic.spells.potion.SpellShield;
import net.braunly.ponymagic.spells.potion.SpellSpeed;
import net.braunly.ponymagic.spells.potion.SpellStaminaHealthRegen;
import net.braunly.ponymagic.spells.simple.SpellEnchant;
import net.braunly.ponymagic.spells.simple.SpellGrow;
import net.braunly.ponymagic.spells.simple.SpellSwish;
import net.braunly.ponymagic.spells.simple.SpellUnEnchant;

public class SpellStorage {
	@Nonnull
	private final ImmutableMap<String, Spell> spells;

	protected SpellStorage() {
		this.spells = new ImmutableMap.Builder<String, Spell>()
				// Custom potions
				.put("antidote", new SpellPotion("antidote"))
				.put("climb", new SpellPotion("climb"))
				.put("dispel", new SpellPotion("dispel"))
				.put("drown", new SpellPotion("drown"))
				.put("purity", new SpellPotion("purity"))
				.put("vulnerable", new SpellPotion("vulnerable"))
				.put("stepup", new SpellPotion("step_up"))
				.put("solidcore", new SpellPotion("solid_core"))
				.put("tpbed", new SpellPotion("teleport_spawn"))
				.put("slowfall", new SpellPotion("slow_fall"))
				.put("cure", new SpellPotion("cure"))
				.put("invert", new SpellPotion("invert"))
				.put("extension", new SpellPotion("extension"))
				.put("revival", new SpellPotion("revival"))
				.put("archery", new SpellPotion("archery"))

				// Custom marker potions
				.put("shield", new SpellShield("shield"))
				.put("shregen", new SpellStaminaHealthRegen("staminahealthregen"))

				// Vanilla potions
				.put("jump", new SpellPotion("jump_boost", true))
				.put("fireresistance", new SpellPotion("fire_resistance", true))
				.put("slow", new SpellPotionSplash("slowness", 2, true))
				.put("nightvision", new SpellPotion("night_vision", true))
				.put("strength", new SpellPotion("strength", true))
				.put("haste", new SpellPotion("haste", true))
				.put("hpregen", new SpellPotion("regeneration", true)) // FIXME: vanilla?
				.put("heal", new SpellPotion("instant_health", true))
				.put("poison", new SpellPotion("poison", true))
				.put("invisibility", new SpellPotion("invisibility", true))

				.put("speed", new SpellSpeed("speed"))

				// Spells
				.put("grow", new SpellGrow("grow"))
				.put("unenchant", new SpellUnEnchant("unenchant"))
				.put("enchant", new SpellEnchant("enchant"))
				.put("swish", new SpellSwish("swish"))
				.build();
	}

	private static class SpellStorageHolder {
		static final SpellStorage INSTANCE = new SpellStorage();
	}

	public static SpellStorage getInstance() {
		return SpellStorageHolder.INSTANCE;
	}

	public Spell getSpell(@Nonnull String name) {
		return spells.get(name);
	}

}