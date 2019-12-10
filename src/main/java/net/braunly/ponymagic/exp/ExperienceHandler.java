package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;

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
			levelData.addExp(expCount);
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
		}
	}
}
