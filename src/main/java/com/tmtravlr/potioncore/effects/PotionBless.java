package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Gives you a random positive potion effect per level.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionBless extends PotionCorePotion {

	public static final String NAME = "bless";
	public static PotionBless instance = null;
	
	public PotionBless(int id) {
		super(id, NAME, false, 0x66CCFF);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	for(int i = 0; i <= amplifier; i++) {
    		PotionCoreHelper.blessEntities.add(entity);
    	}
	}
}
