package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Makes you not affected by poison<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionAntidote extends PotionCorePotion {

	public static final String NAME = "antidote";
	public static PotionAntidote instance = null;
	
	public PotionAntidote(int id) {
		super(id, NAME, false, 0x00CC99);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
