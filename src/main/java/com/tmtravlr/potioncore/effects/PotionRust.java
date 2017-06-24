package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Damages your equipped armor and the item in your hand<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionRust extends PotionCorePotion {
	
	public static final String NAME = "rust";
	public static PotionRust instance = null;
	
	public static int damageTime = 20;
	
	public PotionRust(int id) {
		super(id, NAME, true, 0x773300);
		instance = this;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	if(entity.ticksExisted % damageTime == 0) {
	    	for(int i = 0; i < 5; i++) {
	    		ItemStack stack = entity.getEquipmentInSlot(i);
	    		
	    		if(stack != null && stack.getItem() != null && stack.getItem().isDamageable() && !stack.getItem().getIsRepairable(stack, new ItemStack(Items.gold_ingot))) {
	    			if(!entity.worldObj.isRemote) {
		    			stack.damageItem(amplifier+1, entity);
		    			if(stack.stackSize <= 0) {
		    				entity.setCurrentItemOrArmor(i, null);
		    			}
	    			}
	    		}
	    	}
    	}
	}

}
