package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityKillEventHandler {
    public EntityKillEventHandler() {
        // stub
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onEntityDeath(LivingDeathEvent event) {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer) ||
                event.getEntityLiving().world.isRemote)
            return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ENTITY,
                EntityList.getKey(event.getEntity()),
                0
        );

        String questName = "kill_entity";
        playerData.getLevelData().decreaseGoal(questName, goalConfigKey);

        if (event.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false)) {
            questName = "kill_monster";
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        }

        PonyMagicAPI.playerDataController.savePlayerData(playerData);

    }
}
