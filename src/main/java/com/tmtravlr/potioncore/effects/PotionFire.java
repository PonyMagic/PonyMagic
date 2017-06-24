package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Lights you on fire.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionFire extends PotionCorePotion {

	public static final String NAME = "fire";
	public static PotionFire instance = null;

	public static float fireDuration = 10;
	
	public PotionFire(int id) {
		super(id, NAME, true, 0xFF5500);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
	public void affectEntity(EntityLivingBase thrower, EntityLivingBase entity, int amplifier, double potency) {
		
		int duration = MathHelper.ceiling_double_int((double)(amplifier + 1) * (double)fireDuration * potency);
    	
		entity.setFire(duration);
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	
    	//10 seconds of fire for each level
    	int duration = MathHelper.ceiling_float_int((float)(amplifier+1) * fireDuration);
    	
    	entity.setFire(duration);
    }
}
