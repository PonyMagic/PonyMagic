package net.braunly.ponymagic.spell;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Spell {
	
	public abstract boolean castOnSelf(EntityPlayer player, Integer level);
	
	public abstract boolean castSplash(EntityPlayer player, Integer level, Integer range);
}
