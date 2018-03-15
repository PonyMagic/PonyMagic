package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerDataPacket implements IMessage, IMessageHandler<PlayerDataPacket, IMessage> {
	private NBTTagCompound compound;

	public PlayerDataPacket() {
		// TODO Auto-generated constructor stub
	}

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
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					PlayerData playerData = player.getCapability(PlayerData.PLAYERDATA_CAPABILITY, null);
					playerData.setNBT(message.compound);
					// PonyMagic.log.info("RECIEVED");
					// PonyMagic.log.info(playerData.levelData.getLevel());
				}
			}
		});
		return null;
	}

}
