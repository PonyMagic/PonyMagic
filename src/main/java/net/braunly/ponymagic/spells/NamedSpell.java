package net.braunly.ponymagic.spells;

import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;

public abstract class NamedSpell implements Spell {
	private String spellName;

	protected NamedSpell(String spellName) {
		this.spellName = spellName;
	}

	@Override
	public String getSpellName() {
		return this.spellName;
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig, String[] args) {
		return false;
	}
}
