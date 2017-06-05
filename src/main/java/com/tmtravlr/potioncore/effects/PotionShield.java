package com.tmtravlr.potioncore.effects;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.network.CToSMessage;
import com.tmtravlr.potioncore.network.PacketHandlerServer;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Special shield effect for Pony magic mod.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Braunly
 * @email braunly.brony@gmail.com
 * @Date April 2017
 */
public class PotionShield extends PotionCorePotion {

	public static final String NAME = "shield";
	public static PotionShield instance = null;
	
	public PotionShield(int id) {
		super(id, NAME, false, 0x43B6FF);
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
}
