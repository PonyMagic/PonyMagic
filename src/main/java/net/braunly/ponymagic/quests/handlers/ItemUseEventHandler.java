package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemUseEventHandler {
    public ItemUseEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onItemUse(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        ItemStack itemStack = event.getItem();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                itemStack.getItem().getRegistryName(),
                itemStack.getItemDamage()
        );

        String questName = "use_item";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onBowUse(ArrowLooseEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                event.getBow().getItem().getRegistryName(),
                0
        );

        String questName = "use_item";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
