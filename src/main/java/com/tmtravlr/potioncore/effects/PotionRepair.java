package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Slowly repairs your equipped armor and the item in your hand<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionRepair extends PotionCorePotion {

	public static final String NAME = "repair";
	public static PotionRepair instance = null;
	
	public static int repairTime = 100;
	
	public PotionRepair(int id) {
		super(id, NAME, false, 0x777777);
		instance = this;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	if(entity.ticksExisted % repairTime == 0) {
	    	for(int i = 0; i < 5; i++) {
	    		ItemStack stack = entity.getEquipmentInSlot(i);
	    		if(stack != null && stack.getItem() != null && stack.getItem().isDamageable() && stack.getItem().isDamaged(stack)) {
	    			if(stack.getItemDamage()-(amplifier+1) < 0) {
	    				stack.setItemDamage(0);
	    			}
	    			else {
	    				stack.setItemDamage(stack.getItemDamage()-(amplifier+1));
	    			}
	    		}
	    	}
    	}
	}
}
