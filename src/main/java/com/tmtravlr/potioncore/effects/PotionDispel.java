package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;

/**
 * Removes all good potion effects from you.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionDispel extends PotionCorePotion {

	public static final String NAME = "dispel";
	public static PotionDispel instance = null;
	
	public PotionDispel(int id) {
		super(id, NAME, true, 0x990099);
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
		PotionCoreHelper.dispelEntities.add(entity);
	}
}
