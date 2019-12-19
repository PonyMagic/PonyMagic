package net.braunly.ponymagic.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.swish.ISwishCapability;
import net.braunly.ponymagic.capabilities.swish.SwishProvider;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
					//								player.addExhaustion(Config.flyExhausting); // 0.016

					// Handle fly speed modification
					Potion speedPotion = getVanillaPotion("speed");

					// no inspection ConstantConditions
					if (player.isPotionActive(speedPotion) && player.getActivePotionEffect(speedPotion).getDuration() < 2) {
						updatePlayerFlySpeed(player, 0);
						player.removePotionEffect(speedPotion);
					}
				} else {
					player.fallDistance = 0;
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					player.sendPlayerAbilities();

					// Handle auto slowfall
					if (playerData.getSkillData().isSkillLearned("slowfallauto")) {
						Potion slowFall = SpellPotion.getCustomPotion("slow_fall");
						if (!player.isPotionActive(slowFall)) {
							Integer[] config = Config.potions.get(String.format("%s#%d", "slow_fall_auto", 1));
							player.addPotionEffect(new PotionEffect(slowFall, config[0], config[2]));
						}
					}
				}
			}

			// Handle pegasus swish cooldown
			if (playerData.getSkillData().isSkillLearned("swish")) {
				ISwishCapability swish = player.getCapability(SwishProvider.SWISH, null);
				if (!swish.canSwish() && staminaCurrent > (staminaMaximum/3)) {
					swish.setCanSwish(true);
				}
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
		updatePlayerFlySpeed(event.player, 0);
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
				Integer[] config = Config.potions.get(String.format("shield#%d", 1));
				Double damageModifier = config[2] * 1.0D;
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

	// Player deal damage
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void handleDamagePassive(LivingHurtEvent event) {
		if (event.getEntity().world.isRemote) return;
		
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
			
			if (playerData.getSkillData().isSkillLearned("highground")) {
				if (player.getPosition().getY() - event.getEntity().getPosition().getY() > 1 && player.onGround) {
					event.setAmount(event.getAmount() + ((event.getAmount() / 100.0F) * Config.highgroundDamage));
				}
			}
			
			if (playerData.getSkillData().isSkillLearned("onedge")) {
				IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
				if (stamina.getStamina(EnumStaminaType.CURRENT) < 10) {
					event.setAmount(event.getAmount() + ((event.getAmount() / 100.0F) * Config.onedgeDamage));
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
				if (randNum < Config.dodgingChance) {
					if (playerData.getSkillData().isSkillLearned("dodgingbuff")) {
						player.addPotionEffect(new PotionEffect(SpellPotion.getVanillaPotion("absorption"), 5 * 20));
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
				Integer[] config = Config.passives.get("extinguisher");
				player.addPotionEffect(new PotionEffect(SpellPotion.getVanillaPotion("fire_resistance"), config[0]));
				playerData.getTickData().startTicking("extinguisher", config[1]);
				event.setAmount(0);
				event.setCanceled(true);
			}
			// readyforduel passive
			if (event.getSource().isMagicDamage() &&
					playerData.getSkillData().isSkillLearned("readyforduel") &&
					!playerData.getTickData().isTicking("readyforduel")) {
				Integer[] config = Config.passives.get("readyforduel");
				player.addPotionEffect(new PotionEffect(SpellPotion.getCustomPotion("magic_shield"), config[0], 2));
				playerData.getTickData().startTicking("readyforduel", config[1]);
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
			float flySpeedMod = 0.0F;
			if (playerData.getSkillData().isSkillLearned("flySpeed")) {
				int lvl = playerData.getSkillData().getSkillLevel("flySpeed");
				flySpeedMod = lvl / 100.0F + mod;
			}

			PonyMagic.channel.sendTo(new FlySpeedPacket(flySpeedMod), (EntityPlayerMP) player);
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
