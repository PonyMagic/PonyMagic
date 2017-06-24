package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

/**
 * Teleports you to a random spot nearby.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleport extends PotionCorePotion {

	public static final String NAME = "teleport";
	public static PotionTeleport instance = null;
	
	public static double teleportRange = 16.0;
	
	public PotionTeleport(int id) {
		super(id, NAME, true, 0x00CC99);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        int tries = 20;
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;
        boolean success = false;
        
        while(tries > 0 && !success) {
	    	double d0 = entity.posX + (entity.getRNG().nextDouble() - 0.5D) * teleportRange * (amplifier+1);
	        double d1 = entity.posY + (double)(entity.getRNG().nextInt(64) - 32);
	        double d2 = entity.posZ + (entity.getRNG().nextDouble() - 0.5D) * teleportRange * (amplifier+1);
	        success = this.teleportTo(entity, d0, d1, d2);
	        tries--;
        }
        
        if(!success) {
        	this.teleportTo(entity, posX, posY, posZ);
        }
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
        int xPos = MathHelper.floor_double(entity.posX);
        int yPos = MathHelper.floor_double(entity.posY);
        int zPos = MathHelper.floor_double(entity.posZ);

        if (entity.worldObj.blockExists(xPos, yPos, zPos))
        {
            boolean flag1 = false;

            while (!flag1 && yPos > 0)
            {
            	yPos--;
                Block block = entity.worldObj.getBlock(xPos, yPos, zPos);

                if (block.getMaterial().blocksMovement())
                {
                    flag1 = true;
                    yPos++;
                }
                else
                {
                    --entity.posY;
                }
            }

            if (flag1)
            {
            	entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox))
                {
                    flag = true;
                }
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
