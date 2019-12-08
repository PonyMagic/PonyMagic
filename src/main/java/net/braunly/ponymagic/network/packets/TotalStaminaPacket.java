package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import me.braunly.ponymagic.api.PonyMagicAPI;
import net.braunly.ponymagic.PonyMagic;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TotalStaminaPacket implements IMessage, IMessageHandler<TotalStaminaPacket, IMessage> {
	private Double current;
	private Double maximum;

	public TotalStaminaPacket() {
	}

	public TotalStaminaPacket(IStaminaStorage stamina) {
		this.current = stamina.getStamina(EnumStaminaType.CURRENT);
		this.maximum = stamina.getStamina(EnumStaminaType.MAXIMUM);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.current = buf.readDouble();
		this.maximum = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(this.current);
		buf.writeDouble(this.maximum);
	}

	@Override
	public IMessage onMessage(TotalStaminaPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
					if (stamina != null) {
						stamina.set(EnumStaminaType.CURRENT, message.current);
						stamina.set(EnumStaminaType.MAXIMUM, message.maximum);

						// PonyMagic.log.info("PACKET: " + stamina.getStamina(EnumStaminaType.CURRENT) +
						// "/" + stamina.getStamina(EnumStaminaType.MAXIMUM));
					}
				}
			}
		});
		return null;
	}

}
