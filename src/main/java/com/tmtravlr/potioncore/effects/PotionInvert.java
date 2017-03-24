package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Inverts your potion effects.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionInvert extends PotionCorePotion {

	public static final String NAME = "invert";
	public static PotionInvert instance = null;
	
	public PotionInvert(int id) {
		super(id,NAME, true, 0x99FF99);
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
    	PotionCoreHelper.invertEntities.add(entity);
	}
}
