package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Makes the wither effect not affect you<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionPurity extends PotionCorePotion {

	public static final String NAME = "purity";
	public static PotionPurity instance = null;
	
	public PotionPurity(int id) {
		super(id, NAME, false, 0xFFFFFF);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
