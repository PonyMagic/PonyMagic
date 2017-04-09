package net.braunly.ponymagic.spell;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellAntidote extends Spell {

	@Override
	public boolean castOnSelf(EntityPlayer player, Integer level) {
		Potion potion = PotionCoreHelper.potions.get("potion.antidote");
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.potions.get("antidote")[1])) {
			int dur = Config.potions.get("antidote")[0] * 20;
			player.addPotionEffect(new PotionEffect(potion.getId(), dur, 0));
			return true;
		}
		return false;
	}

	@Override
	public boolean castSplash(EntityPlayer player, Integer level, Integer range) {
		// TODO Auto-generated method stub
		return false;
	}
}
