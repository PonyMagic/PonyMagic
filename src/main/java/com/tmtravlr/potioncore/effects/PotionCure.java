package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;

/**
 * Removes all negative effects from you.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionCure extends PotionCorePotion {

	public static final String NAME = "cure";
	public static PotionCure instance = null;
	
	public PotionCure(int id) {
		super(id, NAME, false, 0xFF55FF);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		PotionCoreHelper.cureEntities.add(entity);
	}
}
