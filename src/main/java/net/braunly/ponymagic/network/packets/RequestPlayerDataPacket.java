package net.braunly.ponymagic.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerDataController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class RequestPlayerDataPacket implements IMessage, IMessageHandler<RequestPlayerDataPacket, IMessage> 
{
	public RequestPlayerDataPacket() {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	@Override
	public IMessage onMessage(RequestPlayerDataPacket message, MessageContext ctx) {
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.SERVER) {
			NBTTagCompound playerDataCompound = PlayerDataController.instance.getPlayerData(player).getNBT();
			PonyMagic.log.info(playerDataCompound);
			PonyMagic.channel.sendTo(new PlayerDataPacket(playerDataCompound), (EntityPlayerMP) player);
		}
		return null;
	}

}
