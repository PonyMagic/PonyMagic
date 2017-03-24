package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * If you die with this effect on, it revives you and heals you 2 hearts.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionRevival extends PotionCorePotion {
	
	public static final String NAME = "revival";
	public static PotionRevival instance = null;
	
	public static float reviveHealth = 4.0f;
	
	public PotionRevival(int id) {
		super(id, NAME, false, 0xFF0000);
		instance = this;
	}
}
