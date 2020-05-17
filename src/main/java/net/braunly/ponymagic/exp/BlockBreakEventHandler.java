package net.braunly.ponymagic.exp;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockBreakEventHandler {
    public BlockBreakEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        String questName = "block_break";
        EntityPlayer player = event.getPlayer();
        String blockName = event.getState().getBlock().getRegistryName().toString();

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        playerData.getLevelData().decreaseGoal(questName, blockName);
        PonyMagicAPI.playerDataController.savePlayerData(playerData);

    }
}
