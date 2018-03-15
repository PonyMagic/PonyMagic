package net.braunly.ponymagic.capabilities.stamina;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IStaminaStorage {

	boolean consume(Double value);

	void add(Double value);

	void set(EnumStaminaType type, Double value);

	void fill();

	void zero();

	Double getStamina(EnumStaminaType type);

	void sync(EntityPlayerMP player);

}