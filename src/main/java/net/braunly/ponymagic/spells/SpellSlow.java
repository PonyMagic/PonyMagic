package net.braunly.ponymagic.spells;

import java.util.List;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellSlow extends Spell {
	
	public SpellSlow() {
		this.spellName = "slow";
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.potions.get(spellName)[1])) {
			int dur = Config.potions.get(spellName)[0] * 20;
			int lvl = Config.potions.get(spellName)[2] - 1;
			int range = 2;
			
			List<EntityLivingBase> entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(range, range, range));
			
			for (EntityLivingBase e: entities) {
//				if (e instanceof EntityPlayer) continue;
				e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), dur, lvl));
			}
			return true;
		}
		return false;
	}
}
