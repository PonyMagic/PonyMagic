package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.network.CToSMessage;
import com.tmtravlr.potioncore.network.PacketHandlerServer;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

/**
 * Lets you climb like a spider.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionClimb extends PotionCorePotion {

	public static final String NAME = "climb";
	public static PotionClimb instance = null;
	
	public PotionClimb(int id) {
		super(id, NAME, false, 0xCC5500);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	if ((entity.isCollidedHorizontally))
    	{
    		if (!entity.isSneaking())
    		{
    			if (entity.moveForward > 0.0f && entity.motionY < 0.2) {
    				entity.motionY = 0.2;
    			}
    		}
    		else {
    			entity.motionY = 0.0;
    		}
    		entity.fallDistance = 0.0f;
    		
    		if(entity.worldObj.isRemote) {
	    		if(entity instanceof EntityPlayer && entity.ticksExisted % 21 == 0) {
	    			PacketBuffer out = new PacketBuffer(Unpooled.buffer());
	    			
	    			out.writeInt(PacketHandlerServer.CLIMB_FALL);
	    			out.writeLong(((EntityPlayer)entity).getUniqueID().getMostSignificantBits());
	    			out.writeLong(((EntityPlayer)entity).getUniqueID().getLeastSignificantBits());
	    			
	    			CToSMessage packet = new CToSMessage(out);
	    			PotionCore.networkWrapper.sendToServer(packet);
	    		}
    		}
    	}
    }
}
