package com.tmtravlr.potioncore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.google.common.base.Function;
import com.tmtravlr.potioncore.effects.PotionAntidote;
import com.tmtravlr.potioncore.effects.PotionArchery;
import com.tmtravlr.potioncore.effects.PotionBless;
import com.tmtravlr.potioncore.effects.PotionCure;
import com.tmtravlr.potioncore.effects.PotionCurse;
import com.tmtravlr.potioncore.effects.PotionDispel;
import com.tmtravlr.potioncore.effects.PotionDrown;
import com.tmtravlr.potioncore.effects.PotionFire;
import com.tmtravlr.potioncore.effects.PotionKlutz;
import com.tmtravlr.potioncore.effects.PotionLevitate;
import com.tmtravlr.potioncore.effects.PotionRepair;
import com.tmtravlr.potioncore.effects.PotionRust;
import com.tmtravlr.potioncore.effects.PotionSlowfall;
import com.tmtravlr.potioncore.effects.PotionVulnerable;
import com.tmtravlr.potioncore.effects.PotionWeight;
import com.tmtravlr.potioncore.potion.ItemPotionCorePotion;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.internal.FMLMessage.EntitySpawnMessage;

public class PotionCoreHelper {

	//Lists used in tick updates
	public static ArrayList<EntityLivingBase> cureEntities = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> dispelEntities = new ArrayList<EntityLivingBase>();
	
	public static ArrayList<EntityLivingBase> blessEntities = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> curseEntities = new ArrayList<EntityLivingBase>();

	public static ArrayList<EntityLivingBase> invertEntities = new ArrayList<EntityLivingBase>();

	public static ArrayList<Potion> goodEffectList = new ArrayList<Potion>();
	public static ArrayList<Potion> badEffectList = new ArrayList<Potion>();
	
	public static HashMap<Potion, Potion> oppositeEffects = new HashMap<Potion, Potion>();
	
	public static HashMap<String, Potion> potions = new HashMap<String, Potion>();
	
	public static final IAttribute projectileDamage = new RangedAttribute("generic.projectileDamage", 1.0D, 0.0D, 2048.0D);

	//Increase the max number of potion ids to 256
	public static void increasePotionTypesSize() {
		Field potionTypes = null;
		FMLLog.info("[Potion Core] Attempting to increase max number of potions.");
		try
		{
			//Get the potion types field from the fortress generation.
			potionTypes = Potion.class.getDeclaredField("field_76425_a");

			//Make it not a final field... (This is so sketchy...)
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(potionTypes, potionTypes.getModifiers() & ~Modifier.FINAL);
		}
		catch (Exception e)
		{
			try
			{
				//Get the potion types field from the fortress generation.
				potionTypes = Potion.class.getDeclaredField("potionTypes");

				//Make it not a final field... (This is so sketchy...)
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(potionTypes, potionTypes.getModifiers() & ~Modifier.FINAL);
			}
			catch (Exception e2)
			{
				FMLLog.warning("[Potion Core] Couldn't increase the maximum number of potions!");
				e.printStackTrace();
				e2.printStackTrace();
			}
		}
		
		if(potionTypes != null) {
			try {
				Potion[] newPotionTypes = new Potion[256];
				
				for(int i = 0; i < newPotionTypes.length && i < Potion.potionTypes.length; i++) {
					newPotionTypes[i] = Potion.potionTypes[i];
				}
				
				potionTypes.set(null, newPotionTypes);
			} catch (Exception e) {
				FMLLog.warning("[Potion Core] Couldn't increase the maximum number of potions!");
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isBadEffect(Potion potion) {
		return ObfuscationReflectionHelper.getPrivateValue(Potion.class, potion, "field_76418_K", "isBadEffect");
	}
	
	//Loads the opposite effects for the inversion potion
	public static void loadInversions() {
		loadInversion(Potion.blindness, Potion.nightVision);
		loadInversion(Potion.damageBoost, Potion.weakness);
		loadInversion(Potion.digSpeed, Potion.digSlowdown);
		loadInversion(Potion.fireResistance, PotionFire.instance);
		loadInversion(Potion.harm, Potion.heal);
		loadInversion(Potion.hunger, Potion.field_76443_y);
		loadInversion(Potion.jump, PotionWeight.instance);
		loadInversion(Potion.moveSlowdown, Potion.moveSpeed);
		loadInversion(Potion.poison, PotionAntidote.instance);
		loadInversion(Potion.regeneration, Potion.wither);
		loadInversion(Potion.resistance, PotionVulnerable.instance);
		loadInversion(Potion.waterBreathing, PotionDrown.instance);
		loadInversion(PotionArchery.instance, PotionKlutz.instance);
		loadInversion(PotionBless.instance, PotionCurse.instance);
		loadInversion(PotionCure.instance, PotionDispel.instance);
		loadInversion(PotionLevitate.instance, PotionSlowfall.instance);
		loadInversion(PotionRepair.instance, PotionRust.instance);
	}
	
	public static void loadInversion(Potion potion1, Potion potion2) {
		
		if(potion1 != null && potion2 != null) {
			oppositeEffects.put(potion1, potion2);
			oppositeEffects.put(potion2, potion1);
		}
	}
	
	/**
	 * Clears all positive effects from the entity
	 */
	public static void clearPositiveEffects(EntityLivingBase entity) {
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
    	ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
    	
    	Iterator<PotionEffect> it = effects.iterator();
    	
    	while(it.hasNext()) {
    		PotionEffect effect = it.next();
    		
    		if(!isBadEffect(Potion.potionTypes[effect.getPotionID()])) {
    			idsToRemove.add(effect.getPotionID());
    		}
    		
    	}
    	
    	for(int id : idsToRemove) {
    		entity.removePotionEffect(id);
    	}
	}
	
	/**
	 * Clears all negative effects from the entity
	 */
	public static void clearNegativeEffects(EntityLivingBase entity) {
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
    	ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
    	
    	Iterator<PotionEffect> it = effects.iterator();
    	
    	while(it.hasNext()) {
    		PotionEffect effect = it.next();
    		
    		if(isBadEffect(Potion.potionTypes[effect.getPotionID()])) {
    			idsToRemove.add(effect.getPotionID());
    		}
    		
    	}
    	
    	for(int id : idsToRemove) {
    		entity.removePotionEffect(id);
    	}
	}
	
	/**
	 * Adds a random positive effect to the entity
	 */
	public static void addPotionEffectPositive(EntityLivingBase entity) {
		int r = entity.getRNG().nextInt(PotionCoreHelper.goodEffectList.size());
		
		Potion potion = PotionCoreHelper.goodEffectList.get(r);
		
		if(potion.isInstant()) {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1));
		}
		else {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1200));
		}
	}
	
	/**
	 * Adds a random negative effect to the entity
	 */
	public static void addPotionEffectNegative(EntityLivingBase entity) {
		int r = entity.getRNG().nextInt(PotionCoreHelper.badEffectList.size());
		
		Potion potion = PotionCoreHelper.badEffectList.get(r);
		
		if(potion.isInstant()) {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1));
		}
		else {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1200));
		}
	}
	
	/**
	 * Inverts the potion effects on the entity based on the map above
	 */
	public static void invertPotionEffects(EntityLivingBase entity) {
		PotionEffect[] effects = new PotionEffect[0];
		effects = (PotionEffect[]) entity.getActivePotionEffects().toArray(effects);
    	
    	for(int i = 0; i < effects.length; i++) {
    		PotionEffect effect = effects[i];
    		
    		if(effect != null && Potion.potionTypes[effect.getPotionID()] != null && oppositeEffects.containsKey(Potion.potionTypes[effect.getPotionID()])) {
    			Potion potion = oppositeEffects.get(Potion.potionTypes[effect.getPotionID()]);
    			
    			int duration = effect.getDuration();
    			
    			if(potion.isInstant()) {
    				duration = 1;
    			}
    			
    			entity.removePotionEffect(effect.getPotionID());
    			entity.addPotionEffect(new PotionEffect(potion.getId(), duration, effect.getAmplifier(), effect.getIsAmbient()));
    		}
    			
    	}
	}
	
	/**
	 * Turns this potion into an {@link ItemStack} that will have it's effect.
	 * @param potion Potion to use
	 * @param duration Duration of the potion
	 * @param amplifier Amplifier of the potion
	 * @param splash Should this be a splash potion?
	 * @return The {@link ItemStack} which has this effect
	 */
	public static ItemStack getItemStack(Potion potion, int duration, int amplifier, boolean splash) {
		ItemStack toAdd;
    	NBTTagCompound tag;
    	
    	tag = new NBTTagCompound();
		tag.setTag("CustomPotionEffects", new NBTTagList());
		NBTTagCompound potionTag = new NBTTagCompound();
		tag.getTagList("CustomPotionEffects", 0).appendTag(new PotionEffect(potion.getId(), duration, amplifier).writeCustomPotionEffectToNBT(potionTag));
		
		toAdd = new ItemStack(ItemPotionCorePotion.instance);
		if(splash) {
			toAdd.setItemDamage(ItemPotionCorePotion.SPLASH_META);
		}
		toAdd.setTagCompound(tag);
		
		return toAdd;
	}
    
    /**
	 * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
	 */
	public static int getCustomPotionColor(Collection<PotionEffect> list) {
		int i = 3694022;

		if (list != null && !list.isEmpty()) {
			
			float red = -1.0F;
			float green = -1.0F;
			float blue = -1.0F;
			float count = 0;
			Iterator<PotionEffect> iterator = list.iterator();

			while (iterator.hasNext()) {
				
				PotionEffect potioneffect = iterator.next();
				int currentPotionColor = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

				float currentRed = (float)(currentPotionColor >> 16 & 255) / 255.0F;
				float currentGreen = (float)(currentPotionColor >> 8 & 255) / 255.0F;
				float currentBlue = (float)(currentPotionColor >> 0 & 255) / 255.0F;

				for(int k = 0; k < potioneffect.getAmplifier()+1; ++k) {
					if(red < 0) {
						red = currentRed;
						green = currentGreen;
						blue = currentBlue;
					}
					else {
						red += currentRed;
						green += currentGreen;
						blue += currentBlue;
						
					}
					count++;
				}
			}

			red = red / count * 255.0F;
			green = green / count * 255.0F;
			blue = blue / count * 255.0F;
			return (int)red << 16 | (int)green << 8 | (int)blue;
		}
		else
		{
			return i;
		}
	}
}
