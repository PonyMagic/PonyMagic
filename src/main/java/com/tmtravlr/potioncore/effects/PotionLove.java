package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

/**
 * Makes animals start breeding<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionLove extends PotionCorePotion {

	public static final String NAME = "love";
	public static PotionLove instance = null;
	
	public PotionLove(int id) {
		super(id, NAME, false, 0xFF3333);
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
    public void affectEntity(EntityLivingBase thrower, EntityLivingBase entity, int amplifier, double potency) {
    	if(entity instanceof EntityAnimal) {
    		EntityPlayer player = null;
    		
    		if(thrower instanceof EntityPlayer) {
    			player = (EntityPlayer) thrower;
    		}
    		
    		((EntityAnimal) entity).func_146082_f(player);
		}
		else {
			performEffect(entity, amplifier);
		}
	}
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		if(entity instanceof EntityAnimal) {
			((EntityAnimal) entity).func_146082_f(null);
		}
		else {
			if(entity.worldObj instanceof WorldServer) {
				((WorldServer)entity.worldObj).func_147487_a("heart", entity.posX, entity.posY, entity.posZ, 20, 0.5, 2, 0.5, 0.0);
			}
		}
	}
}
