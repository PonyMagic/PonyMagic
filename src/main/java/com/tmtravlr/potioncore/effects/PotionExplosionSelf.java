package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;

/**
 * Explodes at your position but does no damage to you.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExplosionSelf extends PotionCorePotion {
	
	public static final String NAME = "burst";
	public static PotionExplosionSelf instance = null;

	public static float explosionSize = 2.0f;
	
	public PotionExplosionSelf(int id) {
		super(id, NAME, false, 0x666666);
		instance = this;
	}

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
	public void affectEntity(EntityLivingBase thrower, EntityLivingBase entity, int amplifier, double potency) {
		
		float strength = (amplifier + 1) * explosionSize * (float)potency;
		
		if(!entity.worldObj.isRemote) {
			entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, strength, false);
		}
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		
		float strength = (amplifier + 1) * explosionSize;
    	
		if(!entity.worldObj.isRemote) {
			entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, strength, false);
		}
    }
}
