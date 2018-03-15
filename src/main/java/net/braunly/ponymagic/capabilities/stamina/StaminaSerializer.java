package net.braunly.ponymagic.capabilities.stamina;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StaminaSerializer implements IStorage<IStaminaStorage> {
	@Override
	public NBTBase writeNBT(Capability<IStaminaStorage> capability, IStaminaStorage instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setDouble("stamina_current", instance.getStamina(EnumStaminaType.CURRENT));
		compound.setDouble("stamina_maximum", instance.getStamina(EnumStaminaType.MAXIMUM));
		return compound;
	}

	@Override
	public void readNBT(Capability<IStaminaStorage> capability, IStaminaStorage instance, EnumFacing side, NBTBase nbt) {
		if (!(instance instanceof StaminaStorage))
			throw new IllegalArgumentException("Can not deserialize to an instance that isn't StaminaStorage");
		instance.set(EnumStaminaType.CURRENT, ((NBTTagCompound) nbt).getDouble("stamina_current"));
		instance.set(EnumStaminaType.MAXIMUM, ((NBTTagCompound) nbt).getDouble("stamina_maximum"));
	}
}
