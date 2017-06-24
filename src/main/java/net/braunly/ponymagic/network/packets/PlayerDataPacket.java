package net.braunly.ponymagic.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataPacket implements IMessage, IMessageHandler<PlayerDataPacket, IMessage> 
{
	private NBTTagCompound compound;

	public PlayerDataPacket() {}
	
	public PlayerDataPacket(NBTTagCompound compound) {
		this.compound = compound;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.compound);
	}

	@Override
	public IMessage onMessage(PlayerDataPacket message, MessageContext ctx) {
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.CLIENT) {
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			playerData.setNBT(message.compound);
			playerData.saveNBTData(null);
			PonyMagic.log.info("RECIEVED");
			PonyMagic.log.info(PlayerDataController.instance.getPlayerData(player).levelData.getLevel());
		}
		return null;
	}

}
