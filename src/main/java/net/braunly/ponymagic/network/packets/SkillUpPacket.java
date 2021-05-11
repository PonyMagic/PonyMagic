package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.skill.Skill;
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
		// Auto-generated constructor stub
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

		thread.addScheduledTask(() -> {
			if (player != null && message.skillName != null) {
				IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
				Skill skillConfig = SkillConfig.getRaceSkill(
						playerData.getRace(),
						message.skillName,
						message.skillLevel
				);
				if (playerData.getSkillData().getSkillLevel(skillConfig.getName()) < skillConfig.getSkillLevel()
						&& playerData.getLevelData().getFreeSkillPoints() >= skillConfig.getPrice()
						&& playerData.getSkillData().isAnySkillLearned(skillConfig.getDepends())
				) {
					playerData.getLevelData().addFreeSkillPoints(-1 * skillConfig.getPrice());
					playerData.getSkillData().upSkillLevel(skillConfig.getName());
					PonyMagicAPI.playerDataController.savePlayerData(playerData);
					MagicHandlersContainer.updatePlayerFlySpeed(playerData, 0.0F);
					MagicHandlersContainer.updatePlayerMaxStamina(playerData);
				}
			}
		});
		return null;
	}

}
