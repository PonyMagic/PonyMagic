package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
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
					PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
					// PonyMagic.log.info("SKILLUP");
					// :FIXME: Check for dependencies and minimum player level
					if (!(playerData.skillData.getSkillLevel(message.skillName) >= message.skillLevel)
							&& playerData.levelData.getFreeSkillPoints() > 0) {
						playerData.skillData.upLevel(message.skillName);
						// PonyMagic.log.info(message.skillName + " " + message.skillLevel);
						playerData.levelData.addFreeSkillPoints(-1);
						playerData.save();
						MagicHandlersContainer.updatePlayerFlySpeed(player, 0);
						MagicHandlersContainer.updatePlayerMaxStamina(player);
					}
				}
			}
		});
		return null;
	}

}
