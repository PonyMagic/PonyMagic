package net.braunly.ponymagic.spell;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellAntidote extends Spell {

	@Override
	public boolean castOnSelf(EntityPlayer player, Integer level) {
		Potion potion = PotionCoreHelper.potions.get("potion.antidote");
		int dur = Config.potions.get("antidote")[0] * 20;
		player.addPotionEffect(new PotionEffect(potion.getId(), dur, 0));
		return true;
	}

	@Override
	public boolean castSplash(EntityPlayer player, Integer level, Integer range) {
		// TODO Auto-generated method stub
		return false;
	}
}
