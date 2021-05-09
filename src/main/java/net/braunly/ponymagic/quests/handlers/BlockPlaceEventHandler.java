package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockPlaceEventHandler {
    public BlockPlaceEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer) || event.getWorld().isRemote) return;

        Block block = event.getState().getBlock();

        String questName = "place_block";
        EntityPlayer player = (EntityPlayer) event.getEntity();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.BLOCK,
                block.getRegistryName(),
                block.getItem(player.world, event.getPos(), event.getState()).getItemDamage()
        );

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        PonyMagicAPI.playerDataController.savePlayerData(playerData);

    }
}
