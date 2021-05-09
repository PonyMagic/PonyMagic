package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.OreDictUtils;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockBreakEventHandler {
    public BlockBreakEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        Block block = event.getState().getBlock();
        int meta = block.getMetaFromState(event.getState());
        
        if (block instanceof BlockOldLog || block instanceof BlockOldLeaf) {
            meta = event.getState().getValue(BlockOldLog.VARIANT).getMetadata();
        }

        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.BLOCK,
                block.getRegistryName(),
                meta
        );

        String questName = "break_block";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        if (OreDictUtils.getInstance().isOre(block.getItem(event.getWorld(), event.getPos(), event.getState()))) {
            questName = "mine_ore";
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        }
        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
