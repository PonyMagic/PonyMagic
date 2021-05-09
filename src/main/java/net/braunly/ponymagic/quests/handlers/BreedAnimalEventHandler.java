package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreedAnimalEventHandler {
    public BreedAnimalEventHandler() {

    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onAnimalBred(BabyEntitySpawnEvent event) {
        EntityPlayer player = event.getCausedByPlayer();

        if (player == null || player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        Entity entity = event.getParentA();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ENTITY,
                EntityList.getKey(entity),
                0
        );

        String questName = "breed_animal";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
