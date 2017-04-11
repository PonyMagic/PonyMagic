package net.braunly.ponymagic.spells;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellNightVision extends Spell {
	
	public SpellNightVision() {
		this.spellName = "nightVision";
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.potions.get(spellName)[1])) {
			int dur = Config.potions.get(spellName)[0] * 20;
			int lvl = Config.potions.get(spellName)[2] - 1;
			player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), dur, lvl));
			return true;
		}
		return false;
	}
}
