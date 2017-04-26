package net.braunly.ponymagic.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.race.EnumRace;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PlayerRacePacket implements IMessage, IMessageHandler<PlayerRacePacket, IMessage> 
{
	private int raceId;

	public PlayerRacePacket() {}
	
	public PlayerRacePacket(int raceId) {
		this.raceId = raceId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		raceId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(raceId);
	}

	@Override
	public IMessage onMessage(PlayerRacePacket message, MessageContext ctx) {
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.CLIENT) {
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			playerData.race = EnumRace.getById(raceId);
			playerData.saveNBTData(null);
		}
		return null;
	}

}
