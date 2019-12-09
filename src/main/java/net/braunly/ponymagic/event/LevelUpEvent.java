package net.braunly.ponymagic.event;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class LevelUpEvent extends Event {

	@Getter
	@Nonnull
	private EntityPlayer player;
	@Getter
	@Nonnull
	private int level;

	@ParametersAreNonnullByDefault
	public LevelUpEvent(EntityPlayer player, int level) {
		this.player = player;
		this.level = level;
	}
}
