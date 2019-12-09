package net.braunly.ponymagic.capabilities.swish;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class SwishProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ISwishCapability.class)
	public static final Capability<ISwishCapability> SWISH = null;
	private ISwishCapability instance = SWISH.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SWISH;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == SWISH ? SWISH.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return SWISH.getStorage().writeNBT(SWISH, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		SWISH.getStorage().readNBT(SWISH, this.instance, null, nbt);
	}

}