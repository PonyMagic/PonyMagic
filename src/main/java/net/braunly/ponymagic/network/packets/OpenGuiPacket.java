package net.braunly.ponymagic.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;

public class OpenGuiPacket implements IMessage, IMessageHandler<OpenGuiPacket, IMessage> 
{
	private int GUI_ID = 0;

	public OpenGuiPacket() {}
	
	public OpenGuiPacket(int GUI_ID) {
		this.GUI_ID = GUI_ID;
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
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.SERVER) {
			player.openGui(PonyMagic.instance,message.GUI_ID, player.worldObj,
					(int) player.posX, (int) player.posY, (int) player.posZ);
		}
		return null;
	}

}