package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;

/**
 * Gives you any random potion effect per level.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionChance extends PotionCorePotion {

	public static final String NAME = "chance";
	public static PotionChance instance = null;
	
	public PotionChance(int id) {
		super(id, NAME, false, 0x0000FF);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	for(int i = 0; i <= amplifier; i++) {
    		if(entity.getRNG().nextBoolean()) {
	     		PotionCoreHelper.blessEntities.add(entity);
	    	}
	    	else {
	    		PotionCoreHelper.curseEntities.add(entity);
	    	}
	   	}
	}
}
