package net.braunly.ponymagic.capabilities.stamina;

import javax.annotation.Nonnull;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.minecraft.entity.player.EntityPlayerMP;

public class StaminaStorage implements IStaminaStorage {

	private Double maximum = 100.0D;
	private Double current = maximum;

	@Override
	public boolean consume(Double amount) {
		if (getStamina(EnumStaminaType.CURRENT) > amount) {
			this.current = this.current - amount;
			return true;
		}
		return false;
	}

	@Override
	public void add(Double amount) {
		this.current = this.current + amount;
	}

	@Override
	public void set(EnumStaminaType type, Double amount) {
		switch (type) {
		case CURRENT:
			if (amount >= getStamina(EnumStaminaType.MAXIMUM))
				amount = getStamina(EnumStaminaType.MAXIMUM);
			this.current = amount;
			break;
		case MAXIMUM:
			if (amount < 0D)
				amount = 0D;
			this.maximum = amount;
			break;
		default:
			throw new NullPointerException("Stamina type not found!");
		}
	}

	@Override
	public void fill() {
		set(EnumStaminaType.CURRENT, getStamina(EnumStaminaType.MAXIMUM));
	}

	@Override
	public void zero() {
		set(EnumStaminaType.CURRENT, 0D);
	}

	@Override
	public Double getStamina(EnumStaminaType type) {
		switch (type) {
		case CURRENT:
			return this.current;
		case MAXIMUM:
			return this.maximum;
		default:
			throw new NullPointerException("Stamina type not found!");
		}
	}

	@Nonnull
	@Override
	public void sync(EntityPlayerMP player) {
		PonyMagic.channel.sendTo(new TotalStaminaPacket(this), player);
	}

}
