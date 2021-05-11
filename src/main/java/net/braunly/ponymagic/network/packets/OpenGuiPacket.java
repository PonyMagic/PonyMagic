package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenGuiPacket implements IMessage, IMessageHandler<OpenGuiPacket, IMessage> {
	private int guiId = 0;

	public OpenGuiPacket() {
		// stub
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		guiId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiId);
	}

	@Override
	public IMessage onMessage(OpenGuiPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(() -> {
			if (player != null) {
				player.openGui(PonyMagic.instance, message.guiId, player.world, (int) player.posX,
						(int) player.posY, (int) player.posZ);
			}
		});
		return null;
	}

}