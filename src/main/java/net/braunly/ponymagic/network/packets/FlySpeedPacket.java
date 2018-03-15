package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FlySpeedPacket implements IMessage, IMessageHandler<FlySpeedPacket, IMessage> {
	private float flySpeed = 0;

	public FlySpeedPacket() {
	}

	public FlySpeedPacket(float flySpeed) {
		this.flySpeed = flySpeed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		flySpeed = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(flySpeed);
	}

	@Override
	public IMessage onMessage(FlySpeedPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					player.capabilities.setFlySpeed(0.05F + message.flySpeed);
					player.sendPlayerAbilities();
				}
			}
		});
		return null;
	}

}
