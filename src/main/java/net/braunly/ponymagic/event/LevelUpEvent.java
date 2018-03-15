package net.braunly.ponymagic.event;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

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
