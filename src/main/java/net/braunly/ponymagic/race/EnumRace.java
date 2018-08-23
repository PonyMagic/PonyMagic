package net.braunly.ponymagic.race;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import net.minecraft.util.text.TextFormatting;

public enum EnumRace {
	REGULAR("Регуляр", TextFormatting.WHITE, "cake", ImmutableSet.of()), 
	PEGASUS("Пегас", TextFormatting.AQUA, "fly",
			ImmutableSet.of(
					"speed",
					"flyspeed",
					"flyduration",
					"haste",
					"slowfallauto",
					"trueshot",
					
					"swish",
					"highground",
					"onedge",
					"dodging",
					"dodgingbuff",
					
					"staminaPool",
					"staminaRegen",
					"shregen"
			)
	), 
	UNICORN("Единорог", TextFormatting.RED, "teleport",
			ImmutableSet.of(
					"teleport",
					"fireresistance",
					"hpregen", "unenchant",
					"solidcore",
					"tpbed",
					
					"shield",
					"heal",
					"enchant",
					"slowfall",

					"tpsurface",
					"recoil",
					"slowfall",
					"revival",
					"fly",
					
					"staminaPool",
					"staminaRegen",
					"shregen"
			)
	), 
	EARTHPONY("Земнопони", TextFormatting.GREEN, "craft",
			ImmutableSet.of(
					"craft",
					"jump",
					"grow",
					"stepup",
					"speed",
					"strength",
					"haste",
					"solidcore",

					"resist",
					"burst",
					"recoil",
					"repair",

					"staminaPool",
					"staminaRegen",
					"shregen"
			)
	), 
	ZEBRA("Зебра", TextFormatting.BLUE, "jump",
			ImmutableSet.of(
					"jump",
					"dispel",
					"fireresistance",
					"drown",
					"slow",
					"purity",
					"nightvision",
					"vulnerable",
					"antidote",
					"climb",
					"poison",
					"invisibility",
					"cure",
					"invert",
					"extension",
					"revival",

					"staminaPool",
					"staminaRegen",
					"shregen"
			)
	);

	private final Set<String> spells;
	@Getter
	private final TextFormatting color;
	@Getter
	private final String localizedName;
	@Getter
	private final String defaultSpell;

	EnumRace(String localizedName, TextFormatting color, String defaultSpell, Set<String> spells) {
		this.localizedName = localizedName; // TODO lang file
		this.defaultSpell = defaultSpell;
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
