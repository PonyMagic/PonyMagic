package com.tmtravlr.potioncore.effects;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Causes you to uncontrollably float upwards<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionLevitate extends PotionCorePotion {

	public static final String NAME = "levitate";
	public static PotionLevitate instance = null;

	public static double floatSpeed = 0.02;
	
	public PotionLevitate(int id) {
		super(id, NAME, true, 0xFFCCFF);
		instance = this;
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	
		entity.addVelocity(0, (double)(amplifier+1) * floatSpeed - entity.motionY, 0);
		entity.velocityChanged = true;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void getCreativeItems(List list) {
    	
		list.add(PotionCoreHelper.getItemStack(this, 30*20, 0, false));
		list.add(PotionCoreHelper.getItemStack(this, 2*60*20, 0, false));
		
		list.add(PotionCoreHelper.getItemStack(this, 20*20, 1, false));
		
		
		list.add(PotionCoreHelper.getItemStack(this, 25*20, 0, true));
		list.add(PotionCoreHelper.getItemStack(this, 1*60*20, 0, true));
		
		list.add(PotionCoreHelper.getItemStack(this, 15*20, 1, true));
		
    	
    }
}
