package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class SmeltedEventHandler {
    public SmeltedEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.HIGH)
    public void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {
        EntityPlayer player = event.player;

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        ItemStack itemStack = event.smelting;
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                itemStack.getItem().getRegistryName(),
                itemStack.getItemDamage()
        );

        for (int i = 0; i < event.smelting.getCount(); i++){
            String questName = "smelt";
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

            if (itemStack.getItem() instanceof ItemFood) {
                questName = "smelt_food";
                playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
            }
        }
        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
