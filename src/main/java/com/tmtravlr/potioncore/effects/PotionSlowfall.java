package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Causes you to float down. At level 1 it removes some fall damage. At level 2 it negates all fall damage.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionSlowfall extends PotionCorePotion {
	
	public static final String NAME = "slowfall";
	public static PotionSlowfall instance = null;
	
	public static double maxSpeed = 0.4;
	
	public PotionSlowfall(int id) {
		super(id, NAME, false, 0xFFFFCC);
		instance = this;
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	double maxMotion = -maxSpeed / (double)(amplifier+1);
    	
		if(entity.motionY < maxMotion) {
			entity.addVelocity(0, maxMotion - entity.motionY, 0);
			entity.velocityChanged = true;
		}
    }
}
