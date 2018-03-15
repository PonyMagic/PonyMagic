package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.event.LevelUpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LevelUpEventHandler {

	public LevelUpEventHandler() {
	}

	@SubscribeEvent
	public void levelUp(LevelUpEvent event) {
		EntityPlayer player = event.getPlayer();

		player.sendMessage(new TextComponentTranslation("event.levelup.string", event.getLevel()));
		player.world.playSound(player, player.getPosition(), MagicSoundHandler.LEVEL_UP, SoundCategory.PLAYERS, 0.5f,
				1f);
		// player.playSound(MagicSoundHandler.LEVEL_UP, 0.5f, 1f);
		player.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, player.posX, player.posY, player.posZ, 1D, 1D,
				1D);
	}
}
