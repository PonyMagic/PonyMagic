package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
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
		// Auto-generated constructor stub
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

		thread.addScheduledTask(() -> {
			if (player != null) {
				IPlayerDataStorage playerData = PonyMagicAPI.getPlayerDataStorage(player);
				playerData.setNBT(message.compound);
			}
		});
		return null;
	}

}
