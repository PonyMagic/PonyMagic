package com.tmtravlr.potioncore.potion;

import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.network.PacketHandlerClient;
import com.tmtravlr.potioncore.network.SToCMessage;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityPotionCorePotion extends EntityPotion {
	
	public ItemStack potion;
	public boolean smashed = false;
	
	public EntityPotionCorePotion(World worldIn)
    {
        super(worldIn);
    }

    public EntityPotionCorePotion(World worldIn, EntityLivingBase throwerIn, int meta)
    {
        this(worldIn, throwerIn, new ItemStack(Items.potionitem, 1, meta));
    }

    public EntityPotionCorePotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionIn)
    {
        super(worldIn, throwerIn, potionIn);
        potion = potionIn;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition position)
    {
        if (!this.worldObj.isRemote)
        {
            List list = Items.potionitem.getEffects(potion);

            if (list != null && !list.isEmpty())
            {
                AxisAlignedBB axisalignedbb = this.boundingBox.expand(4.0D, 2.0D, 4.0D);
                List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

                if (list1 != null && !list1.isEmpty())
                {
                    Iterator iterator = list1.iterator();

                    while (iterator.hasNext())
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
                        double d0 = this.getDistanceSqToEntity(entitylivingbase);

                        if (d0 < 16.0D)
                        {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == position.entityHit)
                            {
                                d1 = 1.0D;
                            }

                            Iterator iterator1 = list.iterator();

                            while (iterator1.hasNext())
                            {
                                PotionEffect potioneffect = (PotionEffect)iterator1.next();
                                int i = potioneffect.getPotionID();

                                if (Potion.potionTypes[i].isInstant())
                                {
                                    Potion.potionTypes[i].affectEntity(this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                                }
                                else
                                {
                                    int j = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);

                                    if (j > 20)
                                    {
                                        entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.worldObj.playSoundAtEntity(this, "game.potion.smash", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            this.setDead();
        }
        else {
	        if(!smashed) {
	        	PotionCore.proxy.doPotionSmashEffects(this.posX, this.posY, this.posZ, potion);
	        	smashed = true;
	        }
        }
    }
    
    public void sendPotionToClient() {
    	PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		
		out.writeInt(PacketHandlerClient.POTION_ENTITY);
		out.writeInt(this.getEntityId());
		try {
			out.writeItemStackToBuffer(potion);
		} catch (IOException e) {
			FMLLog.severe("[Potion Core] Couldn't write potion item stack to packet!");
			e.printStackTrace();
		}
		
		SToCMessage packet = new SToCMessage(out);
		PotionCore.networkWrapper.sendToDimension(packet, this.worldObj.provider.dimensionId);
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
    	super.onUpdate();
    	
    	if(!this.worldObj.isRemote && this.ticksExisted < 5) {
    		this.sendPotionToClient();
    	}
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("Potion", 10))
        {
            this.potion = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
        }
        else
        {
            this.setPotionDamage(tagCompund.getInteger("potionValue"));
        }

        if (this.potion == null)
        {
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.potion != null)
        {
            tagCompound.setTag("Potion", this.potion.writeToNBT(new NBTTagCompound()));
        }
    }

}
