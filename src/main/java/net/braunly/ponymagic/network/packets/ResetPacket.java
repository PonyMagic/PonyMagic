package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
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
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(ResetPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					if (player.experienceLevel >= 30 || player.inventory.hasItemStack(new ItemStack(ModItems.resetBook))) {
						if (player.inventory.hasItemStack(new ItemStack(ModItems.resetBook))) {
							for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				                ItemStack itemstack = player.inventory.getStackInSlot(i);

				                if (itemstack.getItem() instanceof ItemResetBook) {
				                    player.inventory.deleteStack(itemstack);
				                    break;
				                }
				            }
						} else {
							player.addExperienceLevel(-30);
						}
						PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
						playerData.reset();
						playerData.save();
						MagicHandlersContainer.updatePlayerFlySpeed(player, 0);
						MagicHandlersContainer.updatePlayerMaxStamina(player);
					} else {
						player.sendMessage(new TextComponentTranslation("skill.reset.fail", ""));
					}
				}
			}
		});
		return null;
	}

}
