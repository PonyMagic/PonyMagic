package com.tmtravlr.potioncore;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;

import com.tmtravlr.potioncore.effects.PotionAntidote;
import com.tmtravlr.potioncore.effects.PotionArchery;
import com.tmtravlr.potioncore.effects.PotionAttackDamageModified;
import com.tmtravlr.potioncore.effects.PotionBless;
import com.tmtravlr.potioncore.effects.PotionChance;
import com.tmtravlr.potioncore.effects.PotionClimb;
import com.tmtravlr.potioncore.effects.PotionCure;
import com.tmtravlr.potioncore.effects.PotionCurse;
import com.tmtravlr.potioncore.effects.PotionDisorganization;
import com.tmtravlr.potioncore.effects.PotionDispel;
import com.tmtravlr.potioncore.effects.PotionDrown;
import com.tmtravlr.potioncore.effects.PotionExplosion;
import com.tmtravlr.potioncore.effects.PotionExplosionSelf;
import com.tmtravlr.potioncore.effects.PotionExtension;
import com.tmtravlr.potioncore.effects.PotionFire;
import com.tmtravlr.potioncore.effects.PotionFlight;
import com.tmtravlr.potioncore.effects.PotionInvert;
import com.tmtravlr.potioncore.effects.PotionKlutz;
import com.tmtravlr.potioncore.effects.PotionLaunch;
import com.tmtravlr.potioncore.effects.PotionLevitate;
import com.tmtravlr.potioncore.effects.PotionLightning;
import com.tmtravlr.potioncore.effects.PotionLove;
import com.tmtravlr.potioncore.effects.PotionPerplexity;
import com.tmtravlr.potioncore.effects.PotionPurity;
import com.tmtravlr.potioncore.effects.PotionRecoil;
import com.tmtravlr.potioncore.effects.PotionRepair;
import com.tmtravlr.potioncore.effects.PotionRevival;
import com.tmtravlr.potioncore.effects.PotionRust;
import com.tmtravlr.potioncore.effects.PotionShield;
import com.tmtravlr.potioncore.effects.PotionSlowfall;
import com.tmtravlr.potioncore.effects.PotionSolidCore;
import com.tmtravlr.potioncore.effects.PotionSpin;
import com.tmtravlr.potioncore.effects.PotionStepup;
import com.tmtravlr.potioncore.effects.PotionTeleport;
import com.tmtravlr.potioncore.effects.PotionTeleportSpawn;
import com.tmtravlr.potioncore.effects.PotionTeleportSurface;
import com.tmtravlr.potioncore.effects.PotionVulnerable;
import com.tmtravlr.potioncore.effects.PotionWeight;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

import cpw.mods.fml.common.FMLLog;

public class PotionCoreEffects {
	
	public static Potion strengthModified = null;
	
	//Data about each potion
	public static class PotionData {
		public boolean enabled;
		public int id = -1;
		public PotionCorePotion potion = null;
		public Class<? extends PotionCorePotion> potionClass;
		
		public PotionData(int idToSet, Class<? extends PotionCorePotion> classToSet) {
			id = idToSet;
			potionClass = classToSet;
			enabled = true;
		}
	}

	public static HashMap<String, PotionData> potionMap = new HashMap<String, PotionData>();
	
	//Load the default potion options
	static {
		potionMap.put(PotionFire.NAME, new PotionData(-1, PotionFire.class));
		potionMap.put(PotionLightning.NAME, new PotionData(-1, PotionLightning.class));
		potionMap.put(PotionExplosion.NAME, new PotionData(-1, PotionExplosion.class));
		potionMap.put(PotionExplosionSelf.NAME, new PotionData(-1, PotionExplosionSelf.class));
		potionMap.put(PotionWeight.NAME, new PotionData(-1, PotionWeight.class));
		potionMap.put(PotionRecoil.NAME, new PotionData(-1, PotionRecoil.class));
		potionMap.put(PotionDrown.NAME, new PotionData(-1, PotionDrown.class));
		potionMap.put(PotionArchery.NAME, new PotionData(-1, PotionArchery.class));
		potionMap.put(PotionKlutz.NAME, new PotionData(-1, PotionKlutz.class));
		potionMap.put(PotionVulnerable.NAME, new PotionData(-1, PotionVulnerable.class));
		potionMap.put(PotionAntidote.NAME, new PotionData(-1, PotionAntidote.class));
		potionMap.put(PotionPurity.NAME, new PotionData(-1, PotionPurity.class));
		potionMap.put(PotionCure.NAME, new PotionData(-1, PotionCure.class));
		potionMap.put(PotionDispel.NAME, new PotionData(-1, PotionDispel.class));
		potionMap.put(PotionLevitate.NAME, new PotionData(-1, PotionLevitate.class));
		potionMap.put(PotionSlowfall.NAME, new PotionData(-1, PotionSlowfall.class));
		potionMap.put(PotionSolidCore.NAME, new PotionData(-1, PotionSolidCore.class));
		potionMap.put(PotionSpin.NAME, new PotionData(-1, PotionSpin.class));
		potionMap.put(PotionLaunch.NAME, new PotionData(-1, PotionLaunch.class));
		potionMap.put(PotionClimb.NAME, new PotionData(-1, PotionClimb.class));
		potionMap.put(PotionLove.NAME, new PotionData(-1, PotionLove.class));
		potionMap.put(PotionStepup.NAME, new PotionData(-1, PotionStepup.class));
		potionMap.put(PotionPerplexity.NAME, new PotionData(-1, PotionPerplexity.class));
		potionMap.put(PotionDisorganization.NAME, new PotionData(-1, PotionDisorganization.class));
		potionMap.put(PotionRepair.NAME, new PotionData(-1, PotionRepair.class));
		potionMap.put(PotionRust.NAME, new PotionData(-1, PotionRust.class));
		potionMap.put(PotionExtension.NAME, new PotionData(-1, PotionExtension.class));
		potionMap.put(PotionChance.NAME, new PotionData(-1, PotionChance.class));
		potionMap.put(PotionBless.NAME, new PotionData(-1, PotionBless.class));
		potionMap.put(PotionCurse.NAME, new PotionData(-1, PotionCurse.class));
		potionMap.put(PotionFlight.NAME, new PotionData(-1, PotionFlight.class));
		potionMap.put(PotionTeleport.NAME, new PotionData(-1, PotionTeleport.class));
		potionMap.put(PotionTeleportSurface.NAME, new PotionData(-1, PotionTeleportSurface.class));
		potionMap.put(PotionTeleportSpawn.NAME, new PotionData(-1, PotionTeleportSpawn.class));
		potionMap.put(PotionInvert.NAME, new PotionData(-1, PotionInvert.class));
		potionMap.put(PotionRevival.NAME, new PotionData(-1, PotionRevival.class));
		potionMap.put(PotionShield.NAME, new PotionData(-1, PotionShield.class));
	}
	
	public static void loadPotionEffects() {
		
		if(ConfigLoader.modifyStrength) {
			strengthModified = new PotionAttackDamageModified(Potion.damageBoost.getId()).setPotionName("potion.damageBoost").func_111184_a(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2);
		}
		
		for(String name : potionMap.keySet()) {
			PotionData data = potionMap.get(name);
			
			if(!data.enabled) {
				continue;
			}
			
			//If the potion is enabled, create it
			try {
				int prevId = 0;
				if(data.id == -1) {
					int nextId = prevId+1;
					while(nextId < Potion.potionTypes.length && Potion.potionTypes[nextId] != null) {
						nextId++;
						
						if(nextId >= Potion.potionTypes.length) {
							FMLLog.bigWarning("[Potion Core] Ran out of id values at %d; Could not create potion %s!", nextId, name);
							return;
						}
					}
					data.id = nextId;
				}
				
				data.potion = data.potionClass.getConstructor(int.class).newInstance(data.id);
			} catch (InstantiationException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			}
		}
	}
	
}
