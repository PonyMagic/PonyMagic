package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.PotionAttackDamage;

public class PotionAttackDamageModified extends PotionAttackDamage {
	
	public static float modifierStrength = 0.75f;
	
	public PotionAttackDamageModified(int id)
    {
        super(id, false, 9643043);
        this.setIconIndex(4, 0);
    }

	@Override
    public double func_111183_a(int amplifier, AttributeModifier attribute)
    {
        return modifierStrength * (double)(amplifier + 1);
    }
    
}
