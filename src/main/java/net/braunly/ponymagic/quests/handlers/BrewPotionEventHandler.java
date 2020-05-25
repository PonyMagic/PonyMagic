package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class BrewPotionEventHandler {
    public BrewPotionEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onPotionBrewed(PlayerBrewedPotionEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        ItemStack itemStack = event.getStack();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                itemStack.getItem().getRegistryName(),
                itemStack.getItemDamage()
        );

        String questName = "brew_potion";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
