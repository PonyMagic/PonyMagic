package net.braunly.ponymagic.spells;

public abstract class NamedSpell implements Spell {
	private String spellName;

	protected NamedSpell(String spellName) {
		this.spellName = spellName;
	}

	@Override
	public String getSpellName() {
		return this.spellName;
	}
}
