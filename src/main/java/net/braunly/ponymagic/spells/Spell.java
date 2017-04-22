package net.braunly.ponymagic.spells;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Spell {
	public String spellName;
	
	public abstract boolean cast(EntityPlayer player, Integer level);  // FIXME return string
}
