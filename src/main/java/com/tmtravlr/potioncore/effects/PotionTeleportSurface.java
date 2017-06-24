package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

/**
 * Teleports you to the top block in your current x and z coordinates. Doesn't work if you are in a cave-world like the nether.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleportSurface extends PotionCorePotion {
	
	public static final String NAME = "teleportsurface";
	public static PotionTeleportSurface instance = null;
	
	public PotionTeleportSurface(int id) {
		super(id, NAME, false, 0x00FF99);
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
    	int x = MathHelper.floor_double(entity.posX);
    	int z = MathHelper.floor_double(entity.posZ);
    	int y = entity.worldObj.getTopSolidOrLiquidBlock(x, z);
    	
    	if(entity.worldObj.getBlock(x, y - 1, z) == Blocks.bedrock) {
    		y = MathHelper.floor_double(entity.posY);
    	}
    	
        this.teleportTo(entity, x + 0.5D, y + 0.1D, z + 0.5D);
	}
    
    private boolean teleportTo(EntityLivingBase entity, double x, double y, double z) {
    	net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entity, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        entity.posX = event.targetX;
        entity.posY = event.targetY;
        entity.posZ = event.targetZ;
        boolean flag = false;

        if (entity.worldObj.blockExists(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)))
        {
            entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

            if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty())
            {
                flag = true;
            }
        }

        if (!flag)
        {
        	entity.setPosition(d0, d1, d2);
            return false;
        }
        else
        {
        	if(entity.worldObj instanceof WorldServer) {
	            
                ((WorldServer)entity.worldObj).func_147487_a("portal", entity.posX, entity.posY, entity.posZ, 128, 1, 2, 1, 0);
            }

            entity.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
            entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}
