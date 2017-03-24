package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Opposite of a resistance potion (makes damage hurt you more).<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionVulnerable extends PotionCorePotion {

	public static final String NAME = "vulnerable";
	public static PotionVulnerable instance = null;
	
	public static double damageMultiplier = 1.5;
	
	public PotionVulnerable(int id) {
		super(id, NAME, true, 0x557700);
		instance = this;
	}
}
