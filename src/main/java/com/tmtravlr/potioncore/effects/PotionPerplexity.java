package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Reverses your direction keys and jump/sneak<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionPerplexity extends PotionCorePotion {

	public static final String NAME = "perplexity";
	public static final String TAG_NAME = "potion core - perplexity";
	public static PotionPerplexity instance = null;
	
	public PotionPerplexity(int id) {
		super(id, NAME, true, 0x9900FF);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
