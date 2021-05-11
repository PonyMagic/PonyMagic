package net.braunly.ponymagic.capabilities.stamina;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class StaminaProvider implements ICapabilitySerializable<NBTBase> {

	private final IStaminaStorage instance = PonyMagicAPI.STAMINA.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == PonyMagicAPI.STAMINA;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == PonyMagicAPI.STAMINA ? PonyMagicAPI.STAMINA.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return PonyMagicAPI.STAMINA.getStorage().writeNBT(PonyMagicAPI.STAMINA, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		PonyMagicAPI.STAMINA.getStorage().readNBT(PonyMagicAPI.STAMINA, this.instance, null, nbt);
	}

}