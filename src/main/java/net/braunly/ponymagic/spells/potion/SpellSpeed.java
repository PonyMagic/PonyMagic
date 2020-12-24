package net.braunly.ponymagic.spells.potion;

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
			MagicHandlersContainer.updatePlayerFlySpeed(player, flySpeedMod);
			return true;
		}
		return false;
	}
}
