package com.tmtravlr.potioncore;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import com.tmtravlr.potioncore.potion.EntityPotionCorePotion;
import com.tmtravlr.potioncore.potion.ItemPotionCorePotion;
import com.tmtravlr.potioncore.potion.RenderPotionCorePotion;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	Random random = new Random();

	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPotionCorePotion.class, new RenderPotionCorePotion());
	}
	
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new PotionCoreEventHandlerClient());
		FMLCommonHandler.instance().bus().register(new PotionCoreEventHandlerClient());
	}
	
	public void loadInverted() {
		PotionCoreEventHandlerClient.loadInverted();
	}
	
	public void doPotionSmashEffects(double x, double y, double z, ItemStack stack) {
		if(stack == null) {
			return;
		}
		
		int potionColour = PotionCoreHelper.getCustomPotionColor(ItemPotionCorePotion.instance.getEffects(stack));
        float red = (float)(potionColour >> 16 & 255) / 255.0F;
        float green = (float)(potionColour >> 8 & 255) / 255.0F;
        float blue = (float)(potionColour >> 0 & 255) / 255.0F;
        String s1 = "spell";

        if (ItemPotionCorePotion.instance.isEffectInstant(stack))
        {
            s1 = "instantSpell";
        }

        for (int l2 = 0; l2 < 100; ++l2)
        {
            double randRadius = random.nextDouble() * 4.0D;
            double randAngle = random.nextDouble() * Math.PI * 2.0D;
            double xOffset = Math.cos(randAngle) * randRadius;
            double d6 = 0.01D + random.nextDouble() * 0.5D;
            double zOffset = Math.sin(randAngle) * randRadius;
            EntityFX entityfx = spawnPotionParticle(s1, x + xOffset * 0.1D, y + 0.3D, z + zOffset * 0.1D, xOffset, d6, zOffset);

            if (entityfx != null)
            {
                float randMultiplier = 0.75F + random.nextFloat() * 0.25F;
                entityfx.setRBGColorF(red * randMultiplier, green * randMultiplier, blue * randMultiplier);
                entityfx.multiplyVelocity((float)randRadius);
            }
        }
	}
	
	private EntityFX spawnPotionParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ)
    {
		Minecraft mc = Minecraft.getMinecraft();
        EntityFX entityfx = null;
    
        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            int i = mc.gameSettings.particleSetting;

            if (i == 1 && mc.theWorld.rand.nextInt(3) == 0)
            {
                i = 2;
            }

            double d6 = mc.renderViewEntity.posX - x;
            double d7 = mc.renderViewEntity.posY - y;
            double d8 = mc.renderViewEntity.posZ - z;
            
            if (particleName.equals("spell"))
            {
                entityfx = new EntitySpellParticleFX(mc.theWorld, x, y, z, motionX, motionY, motionZ);
            }
            else if (particleName.equals("instantSpell"))
            {
                entityfx = new EntitySpellParticleFX(mc.theWorld, x, y, z, motionX, motionY, motionZ);
                ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
            }
        }
        
        if (entityfx != null)
        {
            mc.effectRenderer.addEffect((EntityFX)entityfx);
        }
        
        return entityfx;
	}
}
