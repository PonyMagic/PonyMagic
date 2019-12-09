package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SkillUpPacket implements IMessage, IMessageHandler<SkillUpPacket, IMessage> {
	private String skillName = null;
	private int skillLevel;

	public SkillUpPacket() {
		// TODO Auto-generated constructor stub
	}

	public SkillUpPacket(String skillName, int skillLevel) {
		this.skillName = skillName;
		this.skillLevel = skillLevel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.skillName = ByteBufUtils.readUTF8String(buf);
		this.skillLevel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.skillName);
		buf.writeInt(skillLevel);
	}

	@Override
	public IMessage onMessage(SkillUpPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null && message.skillName != null) {
					IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
					// PonyMagic.log.info("SKILLUP");
					// :FIXME: Check for dependencies and minimum player level
					if (!(playerData.getSkillData().getSkillLevel(message.skillName) >= message.skillLevel)
							&& playerData.getLevelData().getFreeSkillPoints() > 0) {
						playerData.getSkillData().upSkillLevel(message.skillName);
						// PonyMagic.log.info(message.skillName + " " + message.skillLevel);
						playerData.getLevelData().addFreeSkillPoints(-1);
						PonyMagicAPI.playerDataController.savePlayerData(playerData);
						MagicHandlersContainer.updatePlayerFlySpeed(player, 0);
						MagicHandlersContainer.updatePlayerMaxStamina(player);
					}
				}
			}
		});
		return null;
	}

}
