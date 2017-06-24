package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Lets you fly like in creative mode.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionFlight extends PotionCorePotion {

	public static final String NAME = "flight";
	public static final String TAG_NAME = "potion core - flight";
	public static PotionFlight instance = null;
	
	public PotionFlight(int id) {
		super(id, NAME, false, 0x5599FF);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		
    		player.capabilities.allowFlying = true;
    		
    		if(player.capabilities.isFlying) {
    			entity.fallDistance = 0;
    		}
    	}
    }
}
