package net.braunly.ponymagic.exp;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableMap;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.LevelData;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.event.LevelUpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

abstract class ExperienceHandler {
	abstract ImmutableMap<String, Double> getExperienceTable();

	@ParametersAreNonnullByDefault
	void process(EntityPlayer player, String experienceKey) {
		if (player.world.isRemote)
			return;
		
		double expCount = getExperienceTable().getOrDefault(experienceKey, 0D);

		PlayerData data = PlayerDataController.instance.getPlayerData(player);
		LevelData levelData = data.levelData;

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

		data.save();
	}

}
