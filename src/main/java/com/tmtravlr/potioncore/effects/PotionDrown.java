package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

/**
 * Makes it so you can only breathe water and will drown on land.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionDrown extends PotionCorePotion {

	public static final String NAME = "drown";
	public static final String TAG_NAME = "potioncore - drown air";
	public static final String TAG_BOOLEAN = "potioncore - doing drown";
	public static PotionDrown instance = null;
	
	public PotionDrown(int id) {
		super(id, NAME, true, 0x00FFFF);
		instance = this;
	}
	
	public boolean canAmplify() {
		return false;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
			
		if(!entity.getEntityData().hasKey(TAG_NAME)) {
			entity.getEntityData().setInteger(TAG_NAME, 300);
		}
		
		if (entity.isInsideOfMaterial(Material.water))
	    {
			entity.setAir(300);
			entity.getEntityData().setInteger(TAG_NAME, 300);
	    }
		
		int respiration = EnchantmentHelper.getRespiration(entity);
		int air = entity.getEntityData().getInteger(TAG_NAME);
		air = ((respiration > 0) && (entity.getRNG().nextInt(respiration + 1) > 0) ? air : air - 1);
		
		if (air == -20)
		{
			air = 0;
			if(!entity.worldObj.isRemote) {
				entity.attackEntityFrom(DamageSource.drown, 2.0F);
			}
		}
		entity.getEntityData().setInteger(TAG_NAME, air);
    
	}
}
