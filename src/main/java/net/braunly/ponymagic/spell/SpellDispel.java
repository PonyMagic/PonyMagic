package net.braunly.ponymagic.spell;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellDispel extends Spell {
	
	public SpellDispel() {
		this.spellName = "dispel";
	}

	@Override
	public boolean castOnSelf(EntityPlayer player, Integer level) {
		Potion potion = PotionCoreHelper.potions.get("potion." + spellName);
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.potions.get(spellName)[1])) {
			int dur = Config.potions.get(spellName)[0] * 20;
			int lvl = Config.potions.get(spellName)[2] - 1;
			player.addPotionEffect(new PotionEffect(potion.getId(), dur, lvl));
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
