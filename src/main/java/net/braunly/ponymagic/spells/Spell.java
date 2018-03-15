package net.braunly.ponymagic.spells;

import net.minecraft.entity.player.EntityPlayer;

public interface Spell {
	String getSpellName();

	boolean cast(EntityPlayer player, Integer level);
}
