package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Lets you step up a full block<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionStepup extends PotionCorePotion {

	public static final String NAME = "stepup";
	public static final String TAG_NAME = "potion core - stepup";
	public static final String TAG_DEFAULT = "potion core - stepup default";
	public static PotionStepup instance = null;
	
	public static float increase = 0.5f;
	
	public PotionStepup(int id) {
		super(id, NAME, false, 0x33CC33);
		instance = this;
    }
}
