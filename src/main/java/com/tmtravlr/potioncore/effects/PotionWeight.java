package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Opposite of jump boost. Makes it so you can't jump as high.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionWeight extends PotionCorePotion {
	
	public static final String NAME = "weight";
	public static PotionWeight instance = null;
	
	public static float speedReduction = 0.1F;
	
	public PotionWeight(int id) {
		super(id, NAME, true, 0x555555);
		instance = this;
	}
}
