package net.braunly.ponymagic.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import static net.braunly.ponymagic.spells.potion.SpellPotion.getVanillaPotion;

public class MagicHandlersContainer {
	public MagicHandlersContainer() {
	}

	@SubscribeEvent
	public void handlePlayerUpdate(PlayerTickEvent event) {

		if (event.side == Side.CLIENT)
			return;

		EntityPlayer player = event.player;
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);

		if (player.capabilities.isCreativeMode || player.isSpectator() || playerData.getRace() == EnumRace.REGULAR) {
			// Creatives, spectators and regulars not processing.
			return;
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		Double staminaCurrent = stamina.getStamina(EnumStaminaType.CURRENT);
		Double staminaMaximum = stamina.getStamina(EnumStaminaType.MAXIMUM);

		// Stamina regeneration

		Potion shieldPotion = SpellPotion.getCustomPotion("shield");
		if (staminaCurrent < staminaMaximum
				&& player.getFoodStats().getFoodLevel() > 0
				&& (player.onGround || player.isInWater())
				&& !player.capabilities.isFlying
				&& !player.isPotionActive(shieldPotion)) {
			Double staminaRegen = 0.0D;

			if (player.getFoodStats().getFoodLevel() > Config.lowFoodLevel) {
				staminaRegen = Config.defaultStaminaRegen;
			} else {
				staminaRegen = Config.lowFoodStaminaRegen;
			}

			if (playerData.getSkillData().isSkillLearned("staminaRegen")) {
				int lvl = playerData.getSkillData().getSkillLevel("staminaRegen");
				staminaRegen += lvl / 20.0D; // TODO: config
			}


			if (player.isInWater()) {
				staminaRegen = Config.waterStaminaRegen;
			}

			if (player.isPotionActive(SpellPotion.getCustomPotion("staminahealthregen"))) {
				staminaRegen *= 2;
			}

			player.addExhaustion(0.005f);
			stamina.add(staminaRegen);
			stamina.sync((EntityPlayerMP) player);
		}

		// Take all stamina on low food level
		if (player.getFoodStats().getFoodLevel() == 0 && staminaCurrent > 0) {
			stamina.add(-0.5D);
			stamina.sync((EntityPlayerMP) player);
		}

		if (playerData.getRace() == EnumRace.PEGASUS) {
			if (staminaCurrent > 5) {
				player.capabilities.allowFlying = true;
				player.sendPlayerAbilities();
			}

			if (player.capabilities.isFlying) {
				Double flySpendingValue;

				// Fly duration
				if (playerData.getSkillData().isSkillLearned("flyduration")) {
					int lvl = playerData.getSkillData().getSkillLevel("flyduration");
					flySpendingValue = Config.flySpendingValue - lvl / 50.0D; // TODO: config
				} else {
					flySpendingValue = Config.flySpendingValue;
				}

				if (stamina.consume(flySpendingValue)) { // 0.8 stps
					stamina.sync((EntityPlayerMP) player);
				} else {
					player.fallDistance = 0;
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					player.sendPlayerAbilities();

					// Handle auto slowfall
					if (playerData.getSkillData().isSkillLearned("slowfallauto")) {
						Potion slowFall = SpellPotion.getCustomPotion("slow_fall");
						if (!player.isPotionActive(slowFall)) {
							Skill slowfallautoConfig = SkillConfig.getRaceSkill(EnumRace.PEGASUS, "slowfallauto", 1);
							player.addPotionEffect(new PotionEffect(
									slowFall,
									slowfallautoConfig.getEffect().get("duration"),
									slowfallautoConfig.getEffect().get("duration")
							));
						}
					}
				}
			}

			// Remove fly speed modification from speed skill
			Potion speedPotion = getVanillaPotion("speed");
			if (player.isPotionActive(speedPotion) && player.getActivePotionEffect(speedPotion).getDuration() < 10) {
				updatePlayerFlySpeed(player, 0.0F);
				player.removePotionEffect(speedPotion);
			}

		}
		// Handle timers
		if (playerData.getTickData().isTicking()) {
			// this works twice per tick ._.
			playerData.getTickData().tick();
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handlePegasusFlySpeed(PlayerLoggedInEvent event) {
		updatePlayerFlySpeed(event.player, 0.0F);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void handleMaxStaminaValue(PlayerLoggedInEvent event) {
		updatePlayerMaxStamina(event.player);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void handleStaminaShield(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntity().world.isRemote)
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.capabilities.isCreativeMode) {
			// Creative mode - god mode
			return;
		}

		Potion shieldPotion = SpellPotion.getCustomPotion("shield");
		if (player.isPotionActive(shieldPotion)) {
			IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);

			if (event.getAmount() > 0) {
				Skill shieldConfig = SkillConfig.getRaceSkill(EnumRace.UNICORN, "shield", 1);
				double damageModifier = shieldConfig.getEffect().get("level") * 1.0D;
				if (stamina.consume(event.getAmount() * damageModifier)) {
					event.setAmount(0);
					event.setCanceled(true);
				} else {
					event.setAmount((float) (event.getAmount() - stamina.getStamina(EnumStaminaType.CURRENT)));
					stamina.zero();
					player.removePotionEffect(shieldPotion);
				}
				stamina.sync((EntityPlayerMP) player);
			}
		}
	}
	
	// Passives

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void handleMiningSpeed(PlayerEvent.BreakSpeed event) {
		// NOTE: Needs client side to work. I don't know why
		//if (event.getEntity().world.isRemote) return;

		EntityPlayer player = event.getEntityPlayer();
		IPlayerDataStorage playerData = PonyMagicAPI.getPlayerDataStorage(player);

		// Handle pegasus flyhaste passive
		if (playerData.getRace() == EnumRace.PEGASUS &&
				player.capabilities.isFlying &&
				playerData.getSkillData().isSkillLearned("flyhaste")
		) {
			event.setNewSpeed(player.inventory.getDestroySpeed(event.getState()));
		}
	}


	// Player deal damage
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void handleDamagePassive(LivingHurtEvent event) {
		if (event.getEntity().world.isRemote) return;
		
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
			
			if (playerData.getSkillData().isSkillLearned("highground")) {
				if (player.getPosition().getY() - event.getEntity().getPosition().getY() > 1 && player.onGround) {
					Skill highgroundConfig = SkillConfig.getRaceSkill(EnumRace.PEGASUS, "highground", 1);
					event.setAmount(event.getAmount() +
							((event.getAmount() / 100.0F) *
									highgroundConfig.getSpellData().get("damage_percent"))
					);
				}
			}
			
			if (playerData.getSkillData().isSkillLearned("onedge")) {
				IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
				if (stamina.getStamina(EnumStaminaType.CURRENT) < 10) {
					Skill onedgeConfig = SkillConfig.getRaceSkill(EnumRace.PEGASUS, "onedge", 1);
					event.setAmount(event.getAmount() +
							((event.getAmount() / 100.0F) *
									onedgeConfig.getSpellData().get("damage_percent"))
					);
				}
			}
		}		
	}

	// Player take damage
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void handleDodgingPassive(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntity().world.isRemote)
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.capabilities.isCreativeMode) {
			// Creative mode - god mode
			return;
		}

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);

		if (playerData.getRace() == EnumRace.PEGASUS) {
			if (playerData.getSkillData().isSkillLearned("dodging")) {
				float randNum = player.world.rand.nextFloat() * 100;
				Skill dodgingConfig = SkillConfig.getRaceSkill(EnumRace.PEGASUS, "dodging", 1);
				if (randNum < dodgingConfig.getSpellData().get("chance")) {
					if (playerData.getSkillData().isSkillLearned("dodgingbuff")) {
						Skill dodgingbuffConfig = SkillConfig.getRaceSkill(EnumRace.PEGASUS, "dodgingbuff", 1);
						player.addPotionEffect(new PotionEffect(
								SpellPotion.getVanillaPotion("absorption"),
								dodgingbuffConfig.getEffect().get("duration"),
								dodgingbuffConfig.getEffect().get("level")
						));
					}
					event.setAmount(0);
					event.setCanceled(true);
				}
			}
		} else if (playerData.getRace() == EnumRace.UNICORN) {
			// extinguisher passive
			if (event.getSource().isFireDamage() &&
					playerData.getSkillData().isSkillLearned("extinguisher") &&
					!playerData.getTickData().isTicking("extinguisher")) {
				Skill extinguisherConfig = SkillConfig.getRaceSkill(EnumRace.UNICORN, "extinguisher", 1);
				player.addPotionEffect(new PotionEffect(
						SpellPotion.getVanillaPotion("fire_resistance"),
						extinguisherConfig.getEffect().get("duration"),
						extinguisherConfig.getEffect().get("level")
				));
				playerData.getTickData().startTicking(
						"extinguisher",
						extinguisherConfig.getSpellData().get("cooldown")
				);
				event.setAmount(0);
				event.setCanceled(true);
			}
			// readyforduel passive
			if (event.getSource().isMagicDamage() &&
					playerData.getSkillData().isSkillLearned("readyforduel") &&
					!playerData.getTickData().isTicking("readyforduel")) {
				Skill readyforduelConfig = SkillConfig.getRaceSkill(EnumRace.UNICORN, "readyforduel", 1);
				player.addPotionEffect(new PotionEffect(
						SpellPotion.getCustomPotion("magic_shield"),
						readyforduelConfig.getEffect().get("duration"),
						readyforduelConfig.getEffect().get("level")
				));
				playerData.getTickData().startTicking(
						"readyforduel",
						readyforduelConfig.getSpellData().get("cooldown")
				);
				event.setAmount(0);
				event.setCanceled(true);
			}
		}
	}

	public static void updatePlayerFlySpeed(EntityPlayer player, float mod) {
		if (player.world.isRemote)
			return;

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);

		if (playerData.getRace() == EnumRace.PEGASUS) {
			// Handle passive skill
			float flySpeedMod = 0.0F;
			if (playerData.getSkillData().isSkillLearned("flyspeed")) {
				int lvl = playerData.getSkillData().getSkillLevel("flyspeed");
				flySpeedMod = lvl / 100.0F;
			}

//			player.capabilities.setFlySpeed(0.05F + flySpeedMod + mod);
			PonyMagic.channel.sendTo(new FlySpeedPacket(flySpeedMod + mod), (EntityPlayerMP) player);
		}
	}

	public static void updatePlayerMaxStamina(EntityPlayer player) {
		if (player.world.isRemote)
			return;

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		if (playerData.getSkillData().isSkillLearned("staminaPool")) {
			int lvl = playerData.getSkillData().getSkillLevel("staminaPool");
			stamina.set(EnumStaminaType.MAXIMUM, (double) ((lvl + 2) * 50));
		} else {
			stamina.set(EnumStaminaType.MAXIMUM, 100.0D);  // Default stamina
		}
		stamina.sync((EntityPlayerMP) player);
	}

}
