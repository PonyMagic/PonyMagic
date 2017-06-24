package net.braunly.ponymagic.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class LevelUpEvent extends Event {
	
	private EntityPlayer player;
	private int level;
	
	public LevelUpEvent (EntityPlayer player, int level) {
		this.player = player;
		this.level = level;
	}
	
	public EntityPlayer getPlayer() {
		return this.player;
	}
	
	public int getLevel() {
		return this.level;
	}
}
