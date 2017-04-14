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

public class SpellPotionSplash extends Spell {
	
	private int potionId;
	private int range;
	
	public SpellPotionSplash(String potionName, Integer range) {
		this.spellName = potionName.substring(7);
		this.potionId = PotionCoreHelper.potions.get(potionName).getId();
		this.range = range;
	}
	
	// For vanilla potions
	public SpellPotionSplash(String potionName, Integer potionId, Integer range) {
		this.spellName = potionName;
		this.potionId = potionId;
		this.range = range;
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.potions.get(this.spellName)[1])) {
			int dur = Config.potions.get(this.spellName)[0] * 20;
			int lvl = Config.potions.get(this.spellName)[2] - 1;
			
			List<EntityLivingBase> entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(this.range, this.range, this.range));
			
			for (EntityLivingBase e: entities) {
//				if (e instanceof EntityPlayer) continue;
				e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), dur, lvl));
			}
			return true;
		}
		return false;
	}
}
