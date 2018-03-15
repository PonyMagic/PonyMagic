package net.braunly.ponymagic.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerDataSerializer implements IStorage<PlayerData> {

	@Override
	public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbt) {
		// TODO Auto-generated method stub

	}

}
