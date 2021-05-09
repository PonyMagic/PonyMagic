package net.braunly.ponymagic.spells.potion;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;

public class SpellSpeed extends SpellPotion {

	public SpellSpeed(String spellName) {
		super(spellName, true);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		// Check for cast flood.
		if (player.isPotionActive(getPotion()))
			return false;

		if (action(player, skillConfig)) {
			float flySpeedMod = skillConfig.getSkillLevel() / 80.0F;
			IPlayerDataStorage playerDataStorage = PonyMagicAPI.playerDataController.getPlayerData(player);
			MagicHandlersContainer.updatePlayerFlySpeed(playerDataStorage, flySpeedMod);
			return true;
		}
		return false;
	}
}
