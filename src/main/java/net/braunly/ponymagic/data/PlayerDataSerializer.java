package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerDataSerializer implements IStorage<IPlayerDataStorage> {

	@Override
	public NBTBase writeNBT(Capability<IPlayerDataStorage> capability, IPlayerDataStorage instance, EnumFacing side) {
		// stub
		return null;
	}

	@Override
	public void readNBT(Capability<IPlayerDataStorage> capability, IPlayerDataStorage instance, EnumFacing side, NBTBase nbt) {
		// stub
	}

}
