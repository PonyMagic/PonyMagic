package net.braunly.ponymagic.spells.potion;

import net.minecraft.entity.player.EntityPlayer;

public class SpellStaminaHealthRegen extends SpellPotion {

	public SpellStaminaHealthRegen(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		if (player.isPotionActive(getPotion())) {
			player.removePotionEffect(getPotion());
		} else {
			return action(player, level);
		}
		return false;
	}

}
