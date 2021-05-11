package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.handlers.MagicSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LevelUpSoundPacket implements IMessage, IMessageHandler<LevelUpSoundPacket, IMessage> {
	private int level;

	public LevelUpSoundPacket() {
		// Auto-generated constructor stub
	}

	public LevelUpSoundPacket(int level) {
		this.level = level;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.level = ByteBufUtils.readVarInt(buf, 2);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, level, 2);
	}

	@Override
	public IMessage onMessage(LevelUpSoundPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(() -> {
			if (player != null) {
				player.sendMessage(new TextComponentTranslation("event.levelup.string", message.level));
				player.world.playSound(player, player.getPosition(), MagicSoundHandler.LEVEL_UP, SoundCategory.PLAYERS, 5f, 1f);
			}
		});
		return null;
	}

}
