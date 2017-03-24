package com.tmtravlr.potioncore;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;
import com.tmtravlr.potioncore.effects.PotionAntidote;
import com.tmtravlr.potioncore.effects.PotionBless;
import com.tmtravlr.potioncore.effects.PotionChance;
import com.tmtravlr.potioncore.effects.PotionCurse;
import com.tmtravlr.potioncore.effects.PotionDrown;
import com.tmtravlr.potioncore.effects.PotionFlight;
import com.tmtravlr.potioncore.effects.PotionPurity;
import com.tmtravlr.potioncore.effects.PotionRecoil;
import com.tmtravlr.potioncore.effects.PotionRevival;
import com.tmtravlr.potioncore.effects.PotionSlowfall;
import com.tmtravlr.potioncore.effects.PotionStepup;
import com.tmtravlr.potioncore.effects.PotionTeleportSpawn;
import com.tmtravlr.potioncore.effects.PotionVulnerable;
import com.tmtravlr.potioncore.effects.PotionWeight;
import com.tmtravlr.potioncore.network.PacketHandlerClient;
import com.tmtravlr.potioncore.network.SToCMessage;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class PotionCoreEventHandler {
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if(event.entity instanceof EntityLivingBase) {
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(PotionCoreHelper.projectileDamage);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingJump(LivingJumpEvent event) {
		
		PotionData weight = PotionCoreEffects.potionMap.get(PotionWeight.NAME);
		
		if (weight != null && weight.potion != null && event.entityLiving.isPotionActive(weight.potion)) {
			event.entityLiving.motionY -= (double)((float)(event.entityLiving.getActivePotionEffect(weight.potion).getAmplifier() + 1) * PotionWeight.speedReduction);
        }
		
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingHurt(LivingHurtEvent event) {
		
		double multiplier = 1.0;
			
		if(event.source.isProjectile() && event.source.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase source = (EntityLivingBase) event.source.getEntity();
			
			multiplier *= source.getEntityAttribute(PotionCoreHelper.projectileDamage).getAttributeValue();
			
		}
		
		if (PotionVulnerable.instance != null && event.entityLiving.isPotionActive(PotionVulnerable.instance)) {
			multiplier *= Math.pow(PotionVulnerable.damageMultiplier, event.entityLiving.getActivePotionEffect(PotionVulnerable.instance).getAmplifier()+1);
		}
		
		if(event.source == DamageSource.fall) {
			
			if (PotionSlowfall.instance != null && event.entityLiving.isPotionActive(PotionSlowfall.instance)) {
				int level = event.entityLiving.getActivePotionEffect(PotionSlowfall.instance).getAmplifier();
				
				if(level > 0) {
					event.ammount = -1;
					return;
				}
				
				multiplier *= 0.5;
				if(event.ammount > 20f) {
					event.ammount = 20f;
				}
			}
		}
		
		event.ammount *= multiplier;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingHurtLow(LivingHurtEvent event) {
		
		if(event.source.getSourceOfDamage() instanceof EntityLivingBase) {
			
			if (PotionRecoil.instance != null && event.entityLiving.isPotionActive(PotionRecoil.instance)) {
			//Reflect damage back onto the attacker
			
				float reflectPercent = MathHelper.clamp_float((float)(event.entityLiving.getActivePotionEffect(PotionRecoil.instance).getAmplifier()+1) * PotionRecoil.reflectDamage, PotionRecoil.reflectDamage, 9 * PotionRecoil.reflectDamage);
				
				((EntityLivingBase)event.source.getSourceOfDamage()).attackEntityFrom(DamageSource.causeThornsDamage(event.source.getSourceOfDamage()), event.ammount*reflectPercent);
			
			}
			
        }
	}
	
	@SubscribeEvent
	public void onLivingUpdate (LivingUpdateEvent event) {
		if (PotionDrown.instance != null && event.entityLiving.isPotionActive(PotionDrown.instance)) {
			if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayerMP) {
				if(event.entityLiving.ticksExisted % 20 == 0) {
					PacketBuffer out = new PacketBuffer(Unpooled.buffer());
					
					out.writeInt(PacketHandlerClient.SET_DROWN);
					out.writeInt(event.entity.getEntityData(). getInteger(PotionDrown.TAG_NAME));
					
					SToCMessage packet = new SToCMessage(out);
					PotionCore.networkWrapper.sendTo(packet, (EntityPlayerMP) event.entityLiving);
					
				}
			}
			event.entity.getEntityData().setBoolean(PotionDrown.TAG_BOOLEAN, true);
        }
		else {
			if(event.entity.getEntityData().getBoolean(PotionDrown.TAG_BOOLEAN)) {
				event.entity.getEntityData().setInteger(PotionDrown.TAG_NAME, 300);
			}
		}
		
		if (PotionAntidote.instance != null && event.entityLiving.isPotionActive(PotionAntidote.instance)) {
			event.entityLiving.removePotionEffect(Potion.poison.getId());
		}
		
		if (PotionPurity.instance != null && event.entityLiving.isPotionActive(PotionPurity.instance)) {
			event.entityLiving.removePotionEffect(Potion.wither.getId());
		}
		
		if (PotionSlowfall.instance != null && event.entityLiving.isPotionActive(PotionSlowfall.instance) && event.entityLiving.getActivePotionEffect(PotionSlowfall.instance).getAmplifier() > 0) {
			event.entity.fallDistance = 0.0f;
		}
		
		if(ConfigLoader.fixBlindness) {
			if(event.entityLiving.isPotionActive(Potion.blindness) && event.entityLiving instanceof EntityLiving && ((EntityLiving)event.entityLiving).getAttackTarget() != null) {
				int effectLevel = 3 - event.entityLiving.getActivePotionEffect(Potion.blindness).getAmplifier();
				
				if(effectLevel < 0) {
					effectLevel = 0;
				}

				if(event.entityLiving.getDistanceToEntity(((EntityLiving)event.entityLiving).getAttackTarget()) > effectLevel) {
					((EntityLiving)event.entityLiving).setAttackTarget(null);
				}
			}
		}
		
		if(ConfigLoader.fixInvisibility && event.entityLiving.getRNG().nextInt(60) == 0) {
			if(event.entityLiving instanceof EntityLiving && ((EntityLiving)event.entityLiving).getAttackTarget() != null) {
				EntityLivingBase target = ((EntityLiving)event.entityLiving).getAttackTarget();
				if(target.isPotionActive(Potion.invisibility)) {
					int equipmentCount = 0;

					for(int i = 0; i < 5; i++) {
						if(target.getEquipmentInSlot(i) != null) {
							equipmentCount++;
						}
					}
					
					if(target.getArrowCountInEntity() > 0) {
						equipmentCount += MathHelper.ceiling_float_int((float)target.getArrowCountInEntity() / 10);
					}
					
					if(equipmentCount > 5) equipmentCount = 5;

					if(event.entityLiving.getDistanceToEntity(target) > 1 + 3*equipmentCount) {
						((EntityLiving)event.entityLiving).setAttackTarget(null);
					}
				}
			}
		}
		
		if ((event.entityLiving instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			NBTTagCompound persisted = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persisted);
			}
			
			if (PotionStepup.instance != null && player.isPotionActive(PotionStepup.instance)) {
				
				int effectLevel = player.getActivePotionEffect(PotionStepup.instance).getAmplifier() + 1;
				
				if(effectLevel != persisted.getShort(PotionStepup.TAG_NAME)) {
					float stepHeight = player.stepHeight + PotionStepup.increase * effectLevel;
					
					if(persisted.getShort(PotionStepup.TAG_NAME) == 0) {
						persisted.setFloat(PotionStepup.TAG_DEFAULT, player.stepHeight);
					}
					
					player.stepHeight = stepHeight;
					persisted.setShort(PotionStepup.TAG_NAME, (short) effectLevel);
				}
			}
			else {
				if(persisted.getShort(PotionStepup.TAG_NAME) != 0) {
					player.stepHeight = persisted.getFloat(PotionStepup.TAG_DEFAULT);
					persisted.setShort(PotionStepup.TAG_NAME, (short)0);
				}
			}
			
			if (PotionFlight.instance != null && player.isPotionActive(PotionFlight.instance)) {
				if(!persisted.getBoolean(PotionFlight.TAG_NAME)) {
					persisted.setBoolean(PotionFlight.TAG_NAME, true);
					
					player.capabilities.allowFlying = true;
				}
			}
			else {
				if(persisted.getBoolean(PotionFlight.TAG_NAME)) {
					persisted.setBoolean(PotionFlight.TAG_NAME, false);
					
					player.fallDistance = 0;
					if(!player.capabilities.isCreativeMode) {
						player.capabilities.isFlying = false;
						player.capabilities.allowFlying = false;
					}
				}
			}
			
			if (PotionTeleportSpawn.instance != null && player.isPotionActive(PotionTeleportSpawn.instance)) {
				int spawnDelay = persisted.getInteger(PotionTeleportSpawn.TAG_NAME);
				
				//If player is moving, reset the timer
				if(Math.abs(persisted.getDouble(PotionTeleportSpawn.TAG_X) - player.posX) > 0.01 ||
						Math.abs(persisted.getDouble(PotionTeleportSpawn.TAG_Y) - player.posY) > 0.01 ||
						Math.abs(persisted.getDouble(PotionTeleportSpawn.TAG_Z) - player.posZ) > 0.01) {
					
					persisted.setDouble(PotionTeleportSpawn.TAG_X, player.posX);
					persisted.setDouble(PotionTeleportSpawn.TAG_Y, player.posY);
					persisted.setDouble(PotionTeleportSpawn.TAG_Z, player.posZ);
					
					spawnDelay = 0;
				}
				
				if(spawnDelay++ >= PotionTeleportSpawn.teleportDelay) {
					double initialX = player.posX;
					double initialY = player.posY;
					double initialZ = player.posZ;
					
					if(!player.worldObj.isRemote && player instanceof EntityPlayerMP) {
						int dimension = player.worldObj.provider.dimensionId;
						
						World world = MinecraftServer.getServer().worldServerForDimension(dimension);
				        if (world == null)
				        {
				            dimension = 0;
				        }
				        else if (!world.provider.canRespawnHere())
				        {
				            dimension = world.provider.getRespawnDimension((EntityPlayerMP)player);
				        }
				        
						world = MinecraftServer.getServer().worldServerForDimension(dimension);
				        
				        ChunkCoordinates blockpos = player.getBedLocation(dimension);
				        if(blockpos != null) {
				        	blockpos = EntityPlayer.verifyRespawnCoordinates(world, blockpos, player.isSpawnForced(dimension));
				        }
				        
				        
				        if(blockpos == null) {
				        	blockpos = world.getSpawnPoint();
				        	blockpos.set(blockpos.posX, world.getTopSolidOrLiquidBlock(blockpos.posX, blockpos.posZ), blockpos.posZ);
				        }
				        
				        PotionCoreTeleporter.teleportPlayer((EntityPlayerMP)player, world, blockpos.posX + 0.5D, blockpos.posY + 0.1D, blockpos.posZ + 0.5D);
					}
					
					persisted.setInteger(PotionTeleportSpawn.TAG_NAME, 0);
					player.removePotionEffect(PotionTeleportSpawn.instance.getId());
					
					int maxParticles = 128;

		            for (int i = 0; i < maxParticles; ++i)
		            {
		                double scale = (double)i / ((double)maxParticles - 1.0D);
		                float motionX = (player.getRNG().nextFloat() - 0.5F) * 0.2F;
		                float motionY = (player.getRNG().nextFloat() - 0.5F) * 0.2F;
		                float motionZ = (player.getRNG().nextFloat() - 0.5F) * 0.2F;
		                double posX = initialX + (player.posX - initialX) * scale + (player.getRNG().nextDouble() - 0.5D) * (double)player.width * 2.0D;
		                double posY = initialY + (player.posY - initialY) * scale + player.getRNG().nextDouble() * (double)player.height;
		                double posZ = initialZ + (player.posZ - initialZ) * scale + (player.getRNG().nextDouble() - 0.5D) * (double)player.width * 2.0D;
		                player.worldObj.spawnParticle("portal", posX, posY, posZ, (double)motionX, (double)motionY, (double)motionZ);
		            }
		            
		            player.worldObj.playSoundEffect(initialX, initialY, initialZ, "mob.endermen.portal", 1.0F, 1.0F);
		            player.worldObj.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
					
				}
				else {
					int particles = spawnDelay == 1 ? 0 : MathHelper.ceiling_double_int((double)spawnDelay/10.0D);
					
					for(int i = 0; i < particles; i++) {
						player.worldObj.spawnParticle("fireworksSpark", player.posX + player.getRNG().nextFloat()*2 - 1, player.posY + player.getRNG().nextFloat()*8, player.posZ + player.getRNG().nextFloat()*2 - 1, 0, 0, 0);
					}
					
					persisted.setInteger(PotionTeleportSpawn.TAG_NAME, spawnDelay);
				}
			}
			else {
				if(persisted.getInteger(PotionTeleportSpawn.TAG_NAME) > 0) {
					persisted.setInteger(PotionTeleportSpawn.TAG_NAME, 0);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event) {
		
		if(PotionRevival.instance != null && event.entityLiving.isPotionActive(PotionRevival.instance)) {
			
			int level = event.entityLiving.getActivePotionEffect(PotionRevival.instance).getAmplifier() + 1;
			
			event.setCanceled(true);
			
			event.entityLiving.setHealth(PotionRevival.reviveHealth*level);
			event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "random.levelup", 1.0F, 0.6F);
			
			PacketBuffer out = new PacketBuffer(Unpooled.buffer());
			
			out.writeInt(PacketHandlerClient.REVIVAL_HEARTS);
			out.writeInt(event.entityLiving.getEntityId());
			
			SToCMessage packet = new SToCMessage(out);
			PotionCore.networkWrapper.sendToAllAround(packet, new TargetPoint(event.entityLiving.worldObj.provider.dimensionId, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, 16));
			
			event.entityLiving.removePotionEffect(PotionRevival.instance.getId());
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSetAttackTarget(LivingSetAttackTargetEvent event) {
		if(event.entity instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving) event.entity;

			if(entity.getAttackTarget() != null && event.target != null) {
				if(ConfigLoader.fixInvisibility) {
					if(event.target.isPotionActive(Potion.invisibility)) {
						int equipmentCount = 0;
	
						for(int i = 0; i < 5; i++) {
							if(event.target.getEquipmentInSlot(i) != null) {
								equipmentCount++;
							}
						}
						
						if(event.target.getArrowCountInEntity() > 0) {
							equipmentCount += MathHelper.ceiling_float_int((float)event.target.getArrowCountInEntity() / 10);
						}
						
						if(equipmentCount > 5) equipmentCount = 5;
	
						if(entity.getDistanceToEntity(event.target) > 1 + 3*equipmentCount) {
							entity.setAttackTarget(null);
						}
					}
				}
				
				if(ConfigLoader.fixBlindness) {
					if(entity.isPotionActive(Potion.blindness)) {
						int effectLevel = 3 - entity.getActivePotionEffect(Potion.blindness).getAmplifier();
						
						if(effectLevel < 0) {
							effectLevel = 0;
						}
	
						if(entity.getDistanceToEntity(event.target) > effectLevel) {
							entity.setAttackTarget(null);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		
		if(event.phase == TickEvent.Phase.END) {
			if(!PotionCoreHelper.cureEntities.isEmpty()) {
				
				for(EntityLivingBase entity : PotionCoreHelper.cureEntities) {
					PotionCoreHelper.clearNegativeEffects(entity);
				}
			}
			PotionCoreHelper.cureEntities.clear();
		
			if(!PotionCoreHelper.dispelEntities.isEmpty()) {
				for(EntityLivingBase entity : PotionCoreHelper.dispelEntities) {
					PotionCoreHelper.clearPositiveEffects(entity);
				}
			}
			PotionCoreHelper.dispelEntities.clear();
		
			if(!PotionCoreHelper.blessEntities.isEmpty()) {
				for(EntityLivingBase entity : PotionCoreHelper.blessEntities) {
					PotionCoreHelper.addPotionEffectPositive(entity);
					if(PotionBless.instance != null) {
						entity.removePotionEffect(PotionBless.instance.getId());
					}
					if(PotionChance.instance != null) {
						entity.removePotionEffect(PotionChance.instance.getId());
					}
				}
			}
			PotionCoreHelper.blessEntities.clear();
			
			if(!PotionCoreHelper.curseEntities.isEmpty()) {
				for(EntityLivingBase entity : PotionCoreHelper.curseEntities) {
					PotionCoreHelper.addPotionEffectNegative(entity);
					if(PotionCurse.instance != null) {
						entity.removePotionEffect(PotionCurse.instance.getId());
					}
					if(PotionChance.instance != null) {
						entity.removePotionEffect(PotionChance.instance.getId());
					}
				}
			}
			PotionCoreHelper.curseEntities.clear();
			
			if(!PotionCoreHelper.invertEntities.isEmpty()) {
				for(EntityLivingBase entity : PotionCoreHelper.invertEntities) {
					PotionCoreHelper.invertPotionEffects(entity);
				}
			}
			PotionCoreHelper.invertEntities.clear();
		}
	}
	  
}
