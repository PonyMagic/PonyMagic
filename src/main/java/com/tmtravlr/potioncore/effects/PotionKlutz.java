package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Lowers your projectile damage<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionKlutz extends PotionCorePotion {

	public static final String NAME = "klutz";
	public static PotionKlutz instance = null;

	public static double damageModifier = -0.3;
	
	public PotionKlutz(int id) {
		super(id, NAME, true, 0x999933);
		instance = this;
		this.func_111184_a(PotionCoreHelper.projectileDamage, "fd747754-0718-456c-8538-330c4ab65793", damageModifier, 2);
	}
}
