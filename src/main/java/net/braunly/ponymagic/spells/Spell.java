package net.braunly.ponymagic.spells;

import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;

public interface Spell {
	String getSpellName();

	boolean cast(EntityPlayer player, Skill skillConfig);
}
