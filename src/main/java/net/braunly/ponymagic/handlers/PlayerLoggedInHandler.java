package net.braunly.ponymagic.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLoggedInHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            IPlayerDataStorage playerDataStorage = PonyMagicAPI.playerDataController.getPlayerData(event.player);
            if (playerDataStorage.getRace() == EnumRace.REGULAR) return;

            PonyMagic.channel.sendTo(new PlayerDataPacket(playerDataStorage.getNBT()), (EntityPlayerMP) playerDataStorage.getPlayer());

            MagicHandlersContainer.updatePlayerMaxStamina(playerDataStorage);
            MagicHandlersContainer.updatePlayerFlySpeed(playerDataStorage, 0.0F);
        }
    }
}
