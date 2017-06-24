package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.SharedMonsterAttributes;

/**
 * Causes you to not take knockback<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionSolidCore extends PotionCorePotion {
	
	public static final String NAME = "solidcore";
	public static PotionSolidCore instance = null;
	
	public PotionSolidCore(int id) {
		super(id, NAME, false, 0x222222);
		instance = this;
		this.func_111184_a(SharedMonsterAttributes.knockbackResistance, "9bface7b-f0d0-4bdb-9c0c-09c3237fa99c", 1, 0);
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
