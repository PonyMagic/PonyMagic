package com.tmtravlr.potioncore.effects;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Extends the duration of another potion effect on you.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExtension extends PotionCorePotion {
	
	public static final String NAME = "extension";
	public static PotionExtension instance = null;
	
	public PotionExtension(int id) {
		super(id, NAME, false, 0x990099);
		instance = this;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		ArrayList<PotionEffect> potionList = new ArrayList<PotionEffect>(entity.getActivePotionEffects());
		potionList.remove(entity.getActivePotionEffect(this));
		
		PotionEffect effect;
		for (int i = amplifier+1; potionList.size() > 0 && i-- > 0;) {
			effect = potionList.remove(entity.getRNG().nextInt(potionList.size()));
			if (effect.getPotionID() != this.id && !Potion.potionTypes[effect.getPotionID()].isInstant()) {
				effect.combine(new PotionEffect(effect.getPotionID(), effect.getDuration()+1, effect.getAmplifier(), effect.getIsAmbient()));
			}
		}
	}
}
