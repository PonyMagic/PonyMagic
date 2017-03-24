package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Increases your projectile damage.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionArchery extends PotionCorePotion {

	public static final String NAME = "archery";
	public static PotionArchery instance = null;

	public static double damageModifier = 0.75;
	
	public PotionArchery(int id) {
		super(id, NAME, false, 0x995500);
		instance = this;
		this.func_111184_a(PotionCoreHelper.projectileDamage, "ad83c3cc-e133-41f2-bd3c-c553902f7eca", damageModifier, 2);
	}
}
