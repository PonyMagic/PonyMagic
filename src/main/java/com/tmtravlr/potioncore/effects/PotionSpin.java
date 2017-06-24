package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Causes you to spin uncontrollably in random direcitons<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionSpin extends PotionCorePotion {
	
	public static final String NAME = "spin";
	public static final String TAG_PITCH = "potion core - spin pitch";
	public static final String TAG_YAW = "potion core - spin yaw";
	public static PotionSpin instance = null;
	
	public static float rotationSpeed = 2.0f;
	
	public PotionSpin(int id) {
		super(id, NAME, true, 0x99CC00);
		instance = this;
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	float maxRotation = 8;
    	float maxRand = rotationSpeed * (amplifier+1);
    	
    	float pitch = entity.getEntityData().getFloat(TAG_PITCH);
    	float yaw = entity.getEntityData().getFloat(TAG_YAW);
    	
    	float randPitch = (entity.getRNG().nextFloat() * maxRand * 2) - maxRand;
    	float randYaw = (entity.getRNG().nextFloat() * maxRand * 2) - maxRand;
    	
    	if(MathHelper.abs(pitch + randPitch) > maxRotation) {
    		pitch += -randPitch;
    	}
    	else {
    		pitch += randPitch;
    	}

    	if(MathHelper.abs(yaw + randYaw) > maxRotation) {
    		yaw += -randYaw;
    	}
    	else {
    		yaw += randYaw;
    	}
    	
    	entity.rotationPitch += pitch;
    	entity.rotationYaw += yaw;
    	
    	entity.getEntityData().setFloat(TAG_PITCH, pitch);
    	entity.getEntityData().setFloat(TAG_YAW, yaw);
    }
}
