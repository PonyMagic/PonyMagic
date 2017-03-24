package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Reflects a portion of the damage you take back as thorns damage<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionRecoil extends PotionCorePotion {
	
	public static final String NAME = "recoil";
	public static PotionRecoil instance = null;
	
	public static float reflectDamage = 0.1f;
	
	public PotionRecoil(int id) {
		super(id, NAME, false, 0x008822);
		instance = this;
	}
}
