package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;

/**
 * Explodes at your position but does no damage to you.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExplosion extends PotionCorePotion {
	
	public static final String NAME = "explode";
	public static PotionExplosion instance = null;

	public static float explosionSize = 2.0f;
	
	public PotionExplosion(int id) {
		super(id, NAME, true, 0x333333);
		instance = this;
	}

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
	public void affectEntity(EntityLivingBase thrower, EntityLivingBase entity, int amplifier, double potency) {
		
		float strength = (amplifier + 1) * explosionSize * (float)potency;
		
		EntityLivingBase source = null;
		
		if(thrower instanceof EntityLivingBase) {
			source = (EntityLivingBase) thrower;
		}
    	
		if(!entity.worldObj.isRemote) {
			EntityTNTPrimed tnt = new EntityTNTPrimed(entity.worldObj, entity.posX, entity.posY, entity.posZ, source);
			entity.worldObj.createExplosion(tnt, entity.posX, entity.posY, entity.posZ, strength, true);
		}
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		
		float strength = (amplifier + 1) * explosionSize;
    	
		if(!entity.worldObj.isRemote) {
			EntityTNTPrimed tnt = new EntityTNTPrimed(entity.worldObj, entity.posX, entity.posY, entity.posZ, entity);
			entity.worldObj.createExplosion(tnt, entity.posX, entity.posY, entity.posZ, strength, true);
		}
    }
}
