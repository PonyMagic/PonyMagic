package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class PlayerDataProvider implements ICapabilityProvider {

    private IPlayerDataStorage instance = PonyMagicAPI.PLAYERDATA_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == PonyMagicAPI.PLAYERDATA_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == PonyMagicAPI.PLAYERDATA_CAPABILITY ? PonyMagicAPI.PLAYERDATA_CAPABILITY.cast(this.instance) : null;
    }
}
