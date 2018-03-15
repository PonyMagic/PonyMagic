package net.braunly.ponymagic.capabilities.stamina;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class StaminaProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IStaminaStorage.class)
	public static final Capability<IStaminaStorage> STAMINA = null;
	private IStaminaStorage instance = STAMINA.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == STAMINA;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == STAMINA ? STAMINA.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return STAMINA.getStorage().writeNBT(STAMINA, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		STAMINA.getStorage().readNBT(STAMINA, this.instance, null, nbt);
	}

}