package net.braunly.ponymagic.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;

public class FlySpeedPacket implements IMessage, IMessageHandler<FlySpeedPacket, IMessage> 
{
	private float flySpeed = 0;

	public FlySpeedPacket() {}
	
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
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.CLIENT) {
			player.capabilities.setFlySpeed(0.05F + message.flySpeed);
			player.sendPlayerAbilities();
		}
		return null;
	}

}
