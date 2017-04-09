package net.braunly.ponymagic.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class TotalStaminaPacket implements IMessage, IMessageHandler<TotalStaminaPacket, IMessage> 
{
	private float totalStamina;

	public TotalStaminaPacket() {}
	
	public TotalStaminaPacket(float totalStamina) {
		this.totalStamina = totalStamina;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		totalStamina = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(totalStamina);
	}

	@Override
	public IMessage onMessage(TotalStaminaPacket message, MessageContext ctx) {
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.CLIENT) {
			StaminaPlayer props = StaminaPlayer.get(player);
			props.set(StaminaType.MAXIMUM, totalStamina);
		}
		return null;
	}

}
