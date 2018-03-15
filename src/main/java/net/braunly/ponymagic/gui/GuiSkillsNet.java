package net.braunly.ponymagic.gui;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.braunly.ponymagic.race.EnumRace;

public class GuiSkillsNet {
	@Nonnull
	private final ImmutableMap<EnumRace, Set<GuiButtonSkill>> skillsNet;

	public GuiSkillsNet() {
		Set<GuiButtonSkill> sharedSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				.add(new GuiButtonSkill("staminaPool", 16, 41, 171, 0, 1))
				.add(new GuiButtonSkill("staminaPool", 17, 105, 267, 1, 5))
				.add(new GuiButtonSkill("staminaPool", 18, 233, 267, 2, 15))
				.add(new GuiButtonSkill("staminaRegen", 19, 169, 267, 1, 10))
				.add(new GuiButtonSkill("staminaRegen", 20, 297, 267, 2, 20))
				.add(new GuiButtonSkill("staminaHealthRegen", 21, 361, 267, 1, 25))
				.add(new GuiButtonSkill("staminaFoodRegen", 22, 425, 267, 1, 30))
				.add(new GuiButtonSkill("reset", 100, 20, 318, 0, 1)).build();

		Set<GuiButtonSkill> zebraSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("jump", 0, 41, 97, 0, 1))
				// T1
				.add(new GuiButtonSkill("dispel", ImmutableSet.of(), 1, 105, 65, ImmutableSet.of("slow"), 1, 5))
				.add(new GuiButtonSkill("fireresistance", ImmutableSet.of(), 2, 105, 129, ImmutableSet.of("purity"), 1,
						5))
				.add(new GuiButtonSkill("drown", ImmutableSet.of(), 3, 105, 193, ImmutableSet.of("nightvision"), 1, 5))
				// T2
				.add(new GuiButtonSkill("slow", ImmutableSet.of("dispel"), 4, 169, 65, ImmutableSet.of("vulnerable"), 1,
						10))
				.add(new GuiButtonSkill("purity", ImmutableSet.of("fireresistance"), 5, 169, 129,
						ImmutableSet.of("antidote"), 1, 10))
				.add(new GuiButtonSkill("nightvision", ImmutableSet.of("drown"), 6, 169, 193, ImmutableSet.of("climb"),
						1, 10))
				// T3
				.add(new GuiButtonSkill("vulnerable", ImmutableSet.of("slow"), 7, 233, 65, ImmutableSet.of("poison"), 1,
						15))
				.add(new GuiButtonSkill("antidote", ImmutableSet.of("purity"), 8, 233, 129,
						ImmutableSet.of("poison", "invisibility"), 1, 15))
				.add(new GuiButtonSkill("climb", ImmutableSet.of("nightvision"), 9, 233, 193,
						ImmutableSet.of("invisibility"), 1, 15))
				// T4
				.add(new GuiButtonSkill("poison", ImmutableSet.of("vulnerable", "antidote"), 10, 297, 97,
						ImmutableSet.of("cure"), 1, 20))
				.add(new GuiButtonSkill("invisibility", ImmutableSet.of("antidote", "climb"), 11, 297, 161,
						ImmutableSet.of("cure"), 1, 20))
				// T5
				.add(new GuiButtonSkill("cure", ImmutableSet.of("poison", "invisibility"), 12, 361, 129,
						ImmutableSet.of("invert", "extension", "revival"), 1, 25))
				// T6
				.add(new GuiButtonSkill("invert", ImmutableSet.of("cure"), 13, 425, 65, 1, 30))
				.add(new GuiButtonSkill("extension", ImmutableSet.of("cure"), 14, 425, 129, 1, 30))
				.add(new GuiButtonSkill("revival", ImmutableSet.of("cure"), 15, 425, 193, 1, 30))
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> pegasusSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("fly", 0, 41, 97, 0, 1))
				// T1
				.add(new GuiButtonSkill("speed", ImmutableSet.of(), 1, 105, 97, ImmutableSet.of("flyspeed#1"), 1, 5))
				.add(new GuiButtonSkill("flyduration", ImmutableSet.of(), 2, 105, 161, ImmutableSet.of("flyduration#2"),
						1, 5))
				// T2
				.add(new GuiButtonSkill("flyspeed", ImmutableSet.of("speed#1"), 4, 169, 97,
						ImmutableSet.of("flyspeed#2"), 1, 10))
				.add(new GuiButtonSkill("flyduration", ImmutableSet.of("flyduration#1"), 5, 169, 161,
						ImmutableSet.of("slowfall"), 2, 10))
				// T3
				.add(new GuiButtonSkill("flyspeed", ImmutableSet.of("flyspeed#1"), 7, 233, 97,
						ImmutableSet.of("haste", "speed#2"), 2, 15))
				.add(new GuiButtonSkill("slowfall", ImmutableSet.of("flyduration#2"), 8, 233, 161,
						ImmutableSet.of("haste", "slowfallauto"), 1, 15))
				// T4
				.add(new GuiButtonSkill("speed", ImmutableSet.of("flyspeed#2"), 10, 297, 65, ImmutableSet.of(), 2, 20))
				.add(new GuiButtonSkill("haste", ImmutableSet.of("flyspeed#2", "slowfall"), 11, 297, 129,
						ImmutableSet.of("trueshot"), 1, 20))
				.add(new GuiButtonSkill("slowfallauto", ImmutableSet.of("slowfall"), 11, 297, 193, ImmutableSet.of(), 1,
						20))
				// T5
				.add(new GuiButtonSkill("trueshot", ImmutableSet.of("haste"), 12, 361, 129, ImmutableSet.of(), 1, 25))
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> unicornSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("teleport", 0, 41, 107, 0, 1))
				// T1
				.add(new GuiButtonSkill("fireresistance", ImmutableSet.of(), 1, 105, 43, ImmutableSet.of("solidcore"),
						1, 5))
				.add(new GuiButtonSkill("heal", ImmutableSet.of(), 2, 105, 139, ImmutableSet.of("hpregen#1", "tpbed"),
						1, 5))
				.add(new GuiButtonSkill("unenchant", ImmutableSet.of(), 3, 105, 203, ImmutableSet.of("tpbed"), 1, 5))
				// T2
				.add(new GuiButtonSkill("solidcore", ImmutableSet.of("fireresistance"), 4, 169, 43,
						ImmutableSet.of("shield"), 1, 10))
				.add(new GuiButtonSkill("hpregen", ImmutableSet.of("heal#1"), 5, 169, 107, ImmutableSet.of("heal#2"), 1,
						10))
				.add(new GuiButtonSkill("tpbed", ImmutableSet.of("heal", "unenchant"), 6, 169, 171,
						ImmutableSet.of("heal#2", "enchant"), 1, 10))
				// T3
				.add(new GuiButtonSkill("shield", ImmutableSet.of("solidcore"), 7, 233, 43, ImmutableSet.of(), 1, 15))
				.add(new GuiButtonSkill("heal", ImmutableSet.of("hpregen#1", "tpbed"), 8, 233, 139,
						ImmutableSet.of("hpregen#2", "tpsurface"), 2, 15))
				.add(new GuiButtonSkill("enchant", ImmutableSet.of("tpbed"), 9, 233, 203, ImmutableSet.of("tpsurface"),
						1, 15))
				// T4
				.add(new GuiButtonSkill("hpregen", ImmutableSet.of("heal#2"), 10, 297, 107, ImmutableSet.of("slowfall"),
						2, 20))
				.add(new GuiButtonSkill("tpsurface", ImmutableSet.of("heal#2", "enchant"), 11, 297, 171,
						ImmutableSet.of("slowfall"), 1, 20))
				// T5
				.add(new GuiButtonSkill("recoil", ImmutableSet.of(), 12, 361, 43, ImmutableSet.of("revival"), 1, 25))
				.add(new GuiButtonSkill("slowfall", ImmutableSet.of("hpregen#2", "tpsurface"), 12, 361, 139,
						ImmutableSet.of("fly"), 1, 25))
				// T6
				.add(new GuiButtonSkill("revival", ImmutableSet.of("recoil"), 13, 425, 43, 1, 30))
				.add(new GuiButtonSkill("fly", ImmutableSet.of("cure"), 14, 425, 139, 1, 30))
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> earthponySkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("craft", 0, 41, 97, 0, 1))
				// T1
				.add(new GuiButtonSkill("grow", ImmutableSet.of(), 1, 105, 97, ImmutableSet.of(), 1, 5))
				.add(new GuiButtonSkill("jump", ImmutableSet.of(), 2, 105, 161, ImmutableSet.of("stepup"), 1, 5))
				// T2
				.add(new GuiButtonSkill("stepup", ImmutableSet.of("jump"), 4, 169, 161,
						ImmutableSet.of("strength", "speed"), 1, 10))
				// T3
				.add(new GuiButtonSkill("strength", ImmutableSet.of("stepup"), 7, 233, 129,
						ImmutableSet.of("solidcore"), 1, 15))
				.add(new GuiButtonSkill("speed", ImmutableSet.of("stepup"), 8, 233, 193, ImmutableSet.of("haste"), 1,
						15))
				// T4
				.add(new GuiButtonSkill("solidcore", ImmutableSet.of("strength"), 10, 297, 97,
						ImmutableSet.of("resist"), 1, 20))
				.add(new GuiButtonSkill("haste", ImmutableSet.of("speed"), 11, 297, 193, ImmutableSet.of(), 1, 20))
				// T5
				.add(new GuiButtonSkill("resist", ImmutableSet.of("solidcore"), 12, 361, 97,
						ImmutableSet.of("burst", "recoil"), 1, 25))
				// T6
				.add(new GuiButtonSkill("burst", ImmutableSet.of("resist"), 13, 425, 65, 1, 30))
				.add(new GuiButtonSkill("recoil", ImmutableSet.of("resist"), 14, 425, 129, 1, 30))
				.add(new GuiButtonSkill("repair", ImmutableSet.of(), 15, 425, 193, 1, 30))
				// Add shared skills
				.addAll(sharedSkills).build();

		// Add to map
		this.skillsNet = new ImmutableMap.Builder<EnumRace, Set<GuiButtonSkill>>().put(EnumRace.ZEBRA, zebraSkills)
				.put(EnumRace.PEGASUS, pegasusSkills).put(EnumRace.UNICORN, unicornSkills)
				.put(EnumRace.EARTHPONY, earthponySkills).build();
	}

	private static class GuiSkillsNetHolder {
		static final GuiSkillsNet INSTANCE = new GuiSkillsNet();
	}

	public static GuiSkillsNet getInstance() {
		return GuiSkillsNetHolder.INSTANCE;
	}

	public Set<GuiButtonSkill> getSkillNet(@Nonnull EnumRace race) {
		return this.skillsNet.get(race);
	}

	public GuiButtonSkill getRaceSkill(@Nonnull EnumRace race, String skillName) {
		for (GuiButtonSkill skill : this.skillsNet.get(race)) {
			if (skillName.contains("#")) {
				String[] parts = skillName.split("#");
				if (skill.skillName.equals(parts[0]) && skill.skillLevel == Integer.parseInt(parts[1])) {
					return skill;
				}
			} else if (skill.skillName.equals(skillName)) {
				return skill;
			}
		}
		return null;
	}

}
