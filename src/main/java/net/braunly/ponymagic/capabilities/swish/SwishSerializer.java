package net.braunly.ponymagic.capabilities.swish;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SwishSerializer implements IStorage<ISwishCapability> {
	@Override
	public NBTBase writeNBT(Capability<ISwishCapability> capability, ISwishCapability instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("can_swish", instance.canSwish());
		return compound;
	}

	@Override
	public void readNBT(Capability<ISwishCapability> capability, ISwishCapability instance, EnumFacing side, NBTBase nbt) {
		if (!(instance instanceof SwishStorage))
			throw new IllegalArgumentException("Can not deserialize to an instance that isn't SwishStorage");
		instance.setCanSwish(((NBTTagCompound) nbt).getBoolean("can_swish"));
	}
}
