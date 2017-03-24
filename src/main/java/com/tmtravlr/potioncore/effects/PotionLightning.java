package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Strikes you with lightning<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionLightning extends PotionCorePotion {

	public static final String NAME = "lightning";
	public static PotionLightning instance = null;
	
	public PotionLightning(int id) {
		super(id, NAME, true, 0xFFFF00);
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
    	
    	entity.worldObj.addWeatherEffect(new EntityLightningBolt(entity.worldObj, entity.posX, entity.posY, entity.posZ));
    }

}
