package net.braunly.ponymagic.handlers;

import me.braunly.ponymagic.api.events.LevelUpEvent;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.LevelUpSoundPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LevelUpEventHandler {

	public LevelUpEventHandler() {
	}

	@SubscribeEvent
	public void levelUp(LevelUpEvent event) {
		EntityPlayer player = event.getPlayer();

		if (!player.world.isRemote) {
			PonyMagic.channel.sendTo(new LevelUpSoundPacket(event.getLevel()), (EntityPlayerMP) player);
		}
		player.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, player.posX, player.posY, player.posZ, 1D, 1D,
				1D);
	}
}
