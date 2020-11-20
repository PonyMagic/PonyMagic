package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.util.OreDictUtils;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.BLOCK,
                block.getRegistryName(),
                block.getMetaFromState(event.getState())
        );

        String questName = "break_block";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        if (OreDictUtils.getInstance().isOre(new ItemStack(block))) {
            questName = "mine_ore";
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        }
        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
