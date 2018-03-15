package net.braunly.ponymagic.race;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import net.minecraft.util.text.TextFormatting;

public enum EnumRace {
	REGULAR("Регуляр", TextFormatting.WHITE, ImmutableSet.of()), PEGASUS("Пегас", TextFormatting.AQUA,
			ImmutableSet.of("speed", "flyspeed", "flyduration", "slowfall", "haste", "slowfallauto", "trueshot",

					"staminaPool", "staminaRegen", "staminaHealthRegen", "staminaFoodRegen")), UNICORN("Единорог",
							TextFormatting.RED,
							ImmutableSet.of("teleport", "fireresistance", "hpregen", "unenchant", "solidcore", "tpbed",
									"shield", "heal", "enchant", "slowfall",

									"tpsurface", "recoil", "slowfall", "revival", "fly",

									"staminaPool", "staminaRegen", "staminaHealthRegen",
									"staminaFoodRegen")), EARTHPONY(
											"Земнопони", TextFormatting.GREEN,
											ImmutableSet.of("craft", "jump", "grow", "stepup", "speed", "strength",
													"haste", "solidcore",

													"resist", "burst", "recoil", "repair",

													"staminaPool", "staminaRegen", "staminaHealthRegen",
													"staminaFoodRegen")), ZEBRA(
															"Зебра", TextFormatting.BLUE,
															ImmutableSet.of("jump", "dispel", "fireresistance", "drown",
																	"slow", "purity", "nightvision", "vulnerable",
																	"antidote", "climb", "poison", "invisibility",
																	"cure", "invert", "extension", "revival",

																	"staminaPool", "staminaRegen", "staminaHealthRegen",
																	"staminaFoodRegen"));

	private final Set<String> spells;
	@Getter
	private final TextFormatting color;
	@Getter
	private final String localizedName;

	EnumRace(String localizedName, TextFormatting color, Set<String> spells) {
		this.localizedName = localizedName; // TODO lang file
		this.spells = spells;
		this.color = color;
	}

	public boolean hasSpell(@Nonnull String spellName) {
		return spells.contains(spellName);
	}

	@Nonnull
	public static Optional<EnumRace> getByName(@Nonnull String name) {
		for (EnumRace race : EnumRace.values()) {
			if (race.name().equalsIgnoreCase(name)) {
				return Optional.of(race);
			}
		}
		return Optional.empty();
	}
}
