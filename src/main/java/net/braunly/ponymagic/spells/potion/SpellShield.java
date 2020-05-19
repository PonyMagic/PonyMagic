package net.braunly.ponymagic.spells.potion;

import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;

public class SpellShield extends SpellPotion {

	public SpellShield(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		if (player.isPotionActive(getPotion())) {
			player.removePotionEffect(getPotion());
		} else {
			return action(player, skillConfig);
		}
		return false;
	}

}
