package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ItemUseEventHandler {
    public ItemUseEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onItemUse(LivingEntityUseItemEvent.Start event) {
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
}
