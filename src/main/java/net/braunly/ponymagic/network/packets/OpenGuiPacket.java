package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenGuiPacket implements IMessage, IMessageHandler<OpenGuiPacket, IMessage> {
	private int GUI_ID = 0;

	public OpenGuiPacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		GUI_ID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(GUI_ID);
	}

	@Override
	public IMessage onMessage(OpenGuiPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					player.openGui(PonyMagic.instance, message.GUI_ID, player.world, (int) player.posX,
							(int) player.posY, (int) player.posZ);
				}
			}
		});
		return null;
	}

}