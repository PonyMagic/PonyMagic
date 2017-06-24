package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Shuffles your inventory.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionDisorganization extends PotionCorePotion {

	public static final String NAME = "disorganization";
	public static PotionDisorganization instance = null;
	
	public PotionDisorganization(int id) {
		super(id, NAME, true, 0x9900FF);
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
    	if(!entity.worldObj.isRemote && entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer) entity;
    		int inventoryHalf = player.inventory.mainInventory.length / 2;
    		
    		ItemStack temp;
    		
    		for(int i = 0; i < inventoryHalf; i++) {
    			int slot = player.getRNG().nextInt(inventoryHalf);
    			
    			temp = player.inventory.mainInventory[i];
    			player.inventory.mainInventory[i] = player.inventory.mainInventory[inventoryHalf+slot];
    			player.inventory.mainInventory[inventoryHalf+slot] = temp;
    		}
    		player.inventory.markDirty();
    	}
	}
}
