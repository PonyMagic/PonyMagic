package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AchieveEventHandler {
    public AchieveEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onAchievement(AdvancementEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        Advancement advancement = event.getAdvancement();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.CUSTOM,
                advancement.getId(),
                0
        );

        String questName = "achieve";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
