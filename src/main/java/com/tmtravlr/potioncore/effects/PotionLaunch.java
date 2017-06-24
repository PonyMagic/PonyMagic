package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.network.PacketHandlerClient;
import com.tmtravlr.potioncore.network.SToCMessage;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

/**
 * Launches you into the air<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionLaunch extends PotionCorePotion {
	
	public static final String NAME = "launch";
	public static PotionLaunch instance = null;

	public static double launchSpeed = 1.0;
	
	public PotionLaunch(int id) {
		super(id, NAME, true, 0x00FF00);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {

		entity.motionY = (amplifier+1) * launchSpeed;
		
		if(entity instanceof EntityPlayerMP) {
			PacketBuffer out = new PacketBuffer(Unpooled.buffer());
			
			out.writeInt(PacketHandlerClient.KNOCK_UP);
			out.writeInt(amplifier+1);
			
			SToCMessage packet = new SToCMessage(out);
			PotionCore.networkWrapper.sendTo(packet, (EntityPlayerMP) entity);
		}
    }
}
