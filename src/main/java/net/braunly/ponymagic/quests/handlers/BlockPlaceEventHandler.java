package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockPlaceEventHandler {
    public BlockPlaceEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        String questName = "place_block";
        EntityPlayer player = (EntityPlayer) event.getEntity();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.BLOCK,
                event.getState().getBlock().getRegistryName(),
                event.getState().getBlock().getMetaFromState(event.getState())
        );

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        PonyMagicAPI.playerDataController.savePlayerData(playerData);

    }
}
