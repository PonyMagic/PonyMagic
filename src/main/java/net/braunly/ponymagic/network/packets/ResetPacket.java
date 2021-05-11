package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.items.ItemResetBook;
import net.braunly.ponymagic.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ResetPacket implements IMessage, IMessageHandler<ResetPacket, IMessage> {
	public ResetPacket() {
		// default constructor
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// unused
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// unused
	}

	@Override
	public IMessage onMessage(ResetPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player == null) {
					return;
				}
				if (player.experienceLevel >= Config.getVanillaExpLvlForSkillReset() ||
						player.inventory.hasItemStack(new ItemStack(ModItems.resetBook))) {
					if (player.inventory.hasItemStack(new ItemStack(ModItems.resetBook))) {
						for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
							ItemStack itemstack = player.inventory.getStackInSlot(i);

							if (itemstack.getItem() instanceof ItemResetBook) {
								player.inventory.deleteStack(itemstack);
								break;
							}
						}
					} else {
						player.addExperienceLevel(-1 * Config.getVanillaExpLvlForSkillReset());
					}
					IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
					playerData.reset();
					PonyMagicAPI.playerDataController.savePlayerData(playerData);
					MagicHandlersContainer.updatePlayerFlySpeed(playerData, 0.0F);
					MagicHandlersContainer.updatePlayerMaxStamina(playerData);
				} else {
					player.sendMessage(new TextComponentTranslation("skill.reset.fail", ""));
				}
			}
		});
		return null;
	}

}
