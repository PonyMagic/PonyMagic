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

public class SkillUpPacket implements IMessage, IMessageHandler<SkillUpPacket, IMessage> 
{
	private String skillName = null;
	private int skillLevel;
	
	public SkillUpPacket() {}
	
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
		EntityPlayer player = PonyMagic.proxy.getPlayerFromMessageContext(ctx);
		if (ctx.side == Side.SERVER && message.skillName != null) {
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			PonyMagic.log.info("SKILLUP");
			// :FIXME: Check for dependencies and minimum player level
			if (!(playerData.skillData.getSkillLevel(message.skillName) >= message.skillLevel) &&
					playerData.levelData.getFreeSkillPoints() > 0 ) {
				playerData.skillData.upLevel(message.skillName);
				PonyMagic.log.info(message.skillName + " " + message.skillLevel);
				playerData.levelData.addFreeSkillPoints(-1);
			}			
		}
		return null;
	}

}
