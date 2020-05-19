package net.braunly.ponymagic.gui;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.braunly.ponymagic.api.enums.EnumRace;

import javax.annotation.Nonnull;
import java.util.Set;

public class GuiSkillsNet {
	@Nonnull
	private final ImmutableMap<EnumRace, Set<GuiButtonSkill>> skillsNet;

	public GuiSkillsNet() {
		Set<GuiButtonSkill> sharedSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				.add(new GuiButtonSkill("staminaPool", 16, 41, 267, 0))
				.add(new GuiButtonSkill("staminaPool", 17, 105, 267, 1))
				.add(new GuiButtonSkill("staminaPool", 18, 233, 267, 2))
				.add(new GuiButtonSkill("staminaRegen", 19, 169, 267, 1))
				.add(new GuiButtonSkill("staminaRegen", 20, 297, 267, 2))
				.add(new GuiButtonSkill("shregen", 22, 425, 267, 1))
				.add(new GuiButtonSkill("reset", 100, 20, 318, 0)).build();

		Set<GuiButtonSkill> zebraSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("jump", 0, 41, 203, 1))
				// T1
				.add(new GuiButtonSkill("dispel", 1, 105, 65, ImmutableSet.of("slow"), 1))
				.add(new GuiButtonSkill("fireresistance", 2, 105, 129, ImmutableSet.of("purity"), 1))
				.add(new GuiButtonSkill("drown", 3, 105, 193, ImmutableSet.of("nightvision"), 1))
				// T2
				.add(new GuiButtonSkill("slow", 4, 169, 65, ImmutableSet.of("vulnerable"), 1))
				.add(new GuiButtonSkill("purity", 5, 169, 129,
						ImmutableSet.of("antidote"), 1))
				.add(new GuiButtonSkill("nightvision", 6, 169, 193, ImmutableSet.of("climb"),
						1))
				// T3
				.add(new GuiButtonSkill("vulnerable", 7, 233, 65, ImmutableSet.of("poison"), 1))
				.add(new GuiButtonSkill("antidote", 8, 233, 129,
						ImmutableSet.of("poison", "invisibility"), 1))
				.add(new GuiButtonSkill("climb", 9, 233, 193,
						ImmutableSet.of("invisibility"), 1))
				// T4
				.add(new GuiButtonSkill("poison", 10, 297, 97,
						ImmutableSet.of("cure"), 1))
				.add(new GuiButtonSkill("invisibility", 11, 297, 161,
						ImmutableSet.of("cure"), 1))
				// T5
				.add(new GuiButtonSkill("cure", 12, 361, 129,
						ImmutableSet.of("invert", "extension", "revival"), 1))
				// T6
				.add(new GuiButtonSkill("invert", 13, 425, 65, 1))
				.add(new GuiButtonSkill("extension", 14, 425, 129, 1))
				.add(new GuiButtonSkill("revival", 15, 425, 193, 1))
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> pegasusSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("fly", 0, 41, 203, 0))
				// T1
				.add(new GuiButtonSkill("speed", 1, 105, 97, ImmutableSet.of("flyspeed#1"), 1))
				.add(new GuiButtonSkill("flyduration", 2, 105, 161, ImmutableSet.of("flyduration#2"),
						1))
				// T2
				.add(new GuiButtonSkill("flyspeed", 3, 169, 97,
						ImmutableSet.of("flyspeed#2"), 1))
				.add(new GuiButtonSkill("flyduration", 4, 169, 161,
						ImmutableSet.of("swish"), 2))
				// T3
				.add(new GuiButtonSkill("flyspeed", 5, 233, 97,
						ImmutableSet.of("haste", "highground"), 2))
				.add(new GuiButtonSkill("swish", 6, 233, 161,
						ImmutableSet.of("slowfallauto", "highground"), 1))
				// T4
				.add(new GuiButtonSkill("haste", 7, 297, 65, ImmutableSet.of(), 1))
				.add(new GuiButtonSkill("highground", 8, 297, 129,
						ImmutableSet.of("onedge"), 1))
				.add(new GuiButtonSkill("slowfallauto", 9, 297, 193,
						ImmutableSet.of("dodging"), 1))
				// T5
				.add(new GuiButtonSkill("onedge", 10, 361, 129, ImmutableSet.of("archery"), 1))
				.add(new GuiButtonSkill("dodging", 11, 361, 193,
						ImmutableSet.of("dodgingbuff"), 1))
				// T6
				.add(new GuiButtonSkill("archery", 12, 425, 129, ImmutableSet.of(), 1))
				.add(new GuiButtonSkill("dodgingbuff", 13, 425, 193, ImmutableSet.of(), 1))
				
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> unicornSkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("moonbeam", 17, 41, 75, 1))
				.add(new GuiButtonSkill("portal", 18, 41, 139, 1))
				.add(new GuiButtonSkill("teleport", 0, 41, 203, 0))

				// T1
				.add(new GuiButtonSkill("extinguisher", 1, 105, 43, ImmutableSet.of("shield"),
						1))
				.add(new GuiButtonSkill("hpregen", 2, 105, 139, ImmutableSet.of("hpregen#2", "tpbed"),
						1))
				.add(new GuiButtonSkill("unenchant", 3, 105, 203, ImmutableSet.of("tpbed"), 1))
				// T2
				.add(new GuiButtonSkill("shield", 5, 169, 43, ImmutableSet.of("solidcore"), 1))
				.add(new GuiButtonSkill("hpregen", 5, 169, 107, ImmutableSet.of("heal#1"), 2))
				.add(new GuiButtonSkill("tpbed", 6, 169, 171,
						ImmutableSet.of("enchant"), 1))
				// T3
				.add(new GuiButtonSkill("solidcore", 7, 233, 43, ImmutableSet.of("healwave"), 1))
				.add(new GuiButtonSkill("heal", 8, 233, 107,
						ImmutableSet.of("healwave"), 1))
				.add(new GuiButtonSkill("enchant", 9, 233, 171, ImmutableSet.of("tpsurface"),
						1))
				// T4
				.add(new GuiButtonSkill("healwave", 4, 297, 75,
						ImmutableSet.of("readyforduel"), 1))
				.add(new GuiButtonSkill("tpsurface", 11, 297, 171,
						ImmutableSet.of("slowfall", "blink"), 1))
				// T5
				.add(new GuiButtonSkill("readyforduel", 12, 361, 43, ImmutableSet.of("revival"), 1))
				.add(new GuiButtonSkill("slowfall", 13, 361, 139,
						ImmutableSet.of("fly"), 1))
				.add(new GuiButtonSkill("blink", 14, 361, 203,
						ImmutableSet.of(), 1))
				// T6
				.add(new GuiButtonSkill("revival", 15, 425, 43, 1))
				.add(new GuiButtonSkill("fly", 16, 425, 139, 1))
				// Add shared skills
				.addAll(sharedSkills).build();

		Set<GuiButtonSkill> earthponySkills = new ImmutableSet.Builder<GuiButtonSkill>()
				// Default
				.add(new GuiButtonSkill("craft", 0, 41, 203, 0))
				// T1
				.add(new GuiButtonSkill("grow", 1, 105, 97, ImmutableSet.of(), 1))
				.add(new GuiButtonSkill("jump", 2, 105, 161, ImmutableSet.of("stepup"), 1))
				// T2
				.add(new GuiButtonSkill("stepup", 4, 169, 161,
						ImmutableSet.of("strength", "speed"), 1))
				// T3
				.add(new GuiButtonSkill("strength", 7, 233, 129,
						ImmutableSet.of("solidcore"), 1))
				.add(new GuiButtonSkill("speed", 8, 233, 193, ImmutableSet.of("haste"), 1))
				// T4
				.add(new GuiButtonSkill("solidcore", 10, 297, 97,
						ImmutableSet.of("resist"), 1))
				.add(new GuiButtonSkill("haste", 11, 297, 193, ImmutableSet.of(), 1))
				// T5
				.add(new GuiButtonSkill("resist", 12, 361, 97,
						ImmutableSet.of("ironskin", "recoil"), 1))
				// T6
				.add(new GuiButtonSkill("ironskin", 13, 425, 65, 1))
				.add(new GuiButtonSkill("recoil", 14, 425, 129, 1))
				.add(new GuiButtonSkill("repair", 15, 425, 193, 1))
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
