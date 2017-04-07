package net.braunly.ponymagic.spell;

import java.util.List;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellAntidote extends Spell {

	@Override
	public boolean castOnSelf(EntityPlayer player, Integer level) {
		Potion potion = PotionCoreHelper.potions.get("potion.antidote");
		player.addPotionEffect(new PotionEffect(potion.getId(), 1200, 10));  // TODO config
		return true;
	}

	@Override
	public boolean castSplash(EntityPlayer player, Integer level, Integer range) {
		// TODO Auto-generated method stub
		return false;
	}
}
