package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.event.LevelUpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.ParametersAreNonnullByDefault;

abstract class ExperienceHandler {
	abstract ImmutableMap<String, Double> getExperienceTable();

	@ParametersAreNonnullByDefault
	void process(EntityPlayer player, String experienceKey) {
		if (player.world.isRemote)
			return;
		
		double expCount = getExperienceTable().getOrDefault(experienceKey, 0D);

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		ILevelDataStorage levelData = playerData.getLevelData();

		if (levelData.getLevel() < PonyMagic.MAX_LVL) {
			if (Config.expModifier) {
				expCount *= Config.expModifierAmount;
			}
			levelData.addExp(expCount);

			if (levelData.getExp() >= (levelData.getLevel() + 1) * Config.expPerLevel) {
				levelData.addLevel();
				MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, levelData.getLevel()));
				levelData.resetExp();
				if (levelData.getLevel() % 3 == 0) {
					levelData.addFreeSkillPoints();
				}
			}
		}
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
	}
}
