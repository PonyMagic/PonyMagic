package net.braunly.ponymagic.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

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
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			PonyMagic.channel.sendTo(new PlayerDataPacket(playerData.getNBT()), (EntityPlayerMP) player);
		}
		return null;
	}

}
