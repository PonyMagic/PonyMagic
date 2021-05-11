package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RepairEventHandler {
    public RepairEventHandler() {
        // stub
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onAnvilRepair(AnvilRepairEvent event) {
        ItemStack input = event.getItemInput();
        ItemStack result = event.getItemResult();
        if (!ItemStack.areItemsEqualIgnoreDurability(input, result)) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);

        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                result.getItem().getRegistryName(),
                result.getItemDamage()
        );

        String questName = "repair";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        if (event.getIngredientInput().getItem() instanceof ItemEnchantedBook) {
            questName = "enchant_with_book";
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        }

        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
