package net.braunly.ponymagic.potions;

import com.tmtravlr.potioncore.potion.PotionCorePotion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

/**
 * Special effect for Pony magic mod.<br>
 * <br>
 * Instant: no<br>
 * Amplifier affects it: no
 *
 * @author Braunly
 * @email braunly.brony@gmail.com
 * @Date August 2018
 */
public class PotionStaminaHealthRegen extends PotionCorePotion {

	public static final String NAME = "staminahealthregen";
	public static PotionStaminaHealthRegen instance;

	public PotionStaminaHealthRegen() {
		super(NAME, false, 0xA8789E);
		instance = this;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if (entity.getHealth() > 1) {
			entity.attackEntityFrom(DamageSource.GENERIC, amplifier);
		} else {
			entity.removePotionEffect(this);
		}
	}

	@Override
	public boolean isInstant() {
		return false;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
