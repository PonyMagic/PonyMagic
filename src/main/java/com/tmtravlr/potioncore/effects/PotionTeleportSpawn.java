package com.tmtravlr.potioncore.effects;

import java.util.List;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Teleports you to your spawn point if you don't move for 10 seconds.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleportSpawn extends PotionCorePotion {

	public static final String NAME = "teleportspawn";
	public static final String TAG_NAME = "potion core - spawn teleport";
	public static final String TAG_X = "potion core - spawn teleport x";
	public static final String TAG_Y = "potion core - spawn teleport y";
	public static final String TAG_Z = "potion core - spawn teleport z";
	public static PotionTeleportSpawn instance = null;
	
	public static int teleportDelay = 200;
	
	public PotionTeleportSpawn(int id) {
		super(id, NAME, false, 0x9955FF);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean canAmplify() {
        return false;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void getCreativeItems(List list) {
    	
		list.add(PotionCoreHelper.getItemStack(this, 30*20, 0, false));
		
		list.add(PotionCoreHelper.getItemStack(this, 20*20, 0, true));
		
    }
	
}
