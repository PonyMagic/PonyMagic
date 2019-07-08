package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.stamina.EnumStaminaType;
import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.capabilities.swish.ISwishCapability;
import net.braunly.ponymagic.capabilities.swish.SwishProvider;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
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

		EntityPlayer player = event.player;// getEntityLiving();
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);

		if (player.capabilities.isCreativeMode || player.isSpectator() || playerData.race == EnumRace.REGULAR) {
			// Creatives, spectators and regulars not processing.
			return;
		}

		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		Double staminaCurrent = stamina.getStamina(EnumStaminaType.CURRENT);
		Double staminaMaximum = stamina.getStamina(EnumStaminaType.MAXIMUM);

		// Stamina regeneration

		Potion shieldPotion = SpellPotion.getCustomPotion("shield");
		if (staminaCurrent < staminaMaximum
				&& player.getFoodStats().getFoodLevel() > 0
				&& (player.onGround || player.isInWater())
				&& !player.isPotionActive(shieldPotion)) {
			Double staminaRegen = 0.0D;

			if (player.getFoodStats().getFoodLevel() > Config.lowFoodLevel) {
				staminaRegen = Config.defaultStaminaRegen;
			} else {
				staminaRegen = Config.lowFoodStaminaRegen;
			}

			if (playerData.skillData.isSkillLearned("staminaRegen")) {
				int lvl = playerData.skillData.getSkillLevel("staminaRegen");
				staminaRegen += lvl / 20.0D; // TODO: config
			}


			if (player.isInWater() && !player.capabilities.isFlying) {
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

		if (playerData.race == EnumRace.PEGASUS) {
			if (staminaCurrent > 5) {
				player.capabilities.allowFlying = true;
				player.sendPlayerAbilities();
			}

			if (player.capabilities.isFlying) {
				Double flySpendingValue;

				// Fly duration
				if (playerData.skillData.isSkillLearned("flyduration")) {
					int lvl = playerData.skillData.getSkillLevel("flyduration");
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
					if (playerData.skillData.isSkillLearned("slowfallauto")) {
						Potion slowFall = SpellPotion.getCustomPotion("slow_fall");
						if (!player.isPotionActive(slowFall)) {
							Integer[] config = Config.potions.get(String.format("%s#%d", "slow_fall_auto", 1));
							player.addPotionEffect(new PotionEffect(slowFall, config[0] * SpellPotion.TPS, config[2]));
						}
					}
				}
			}

			if (playerData.skillData.isSkillLearned("swish")) {
				ISwishCapability swish = player.getCapability(SwishProvider.SWISH, null);
				if (!swish.canSwish() && staminaCurrent > (staminaMaximum/3)) {
					swish.setCanSwish(true);
				}
			}

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
			IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);

			if (event.getAmount() > 0) {
				Integer[] config = Config.potions.get(String.format("shield#%d", 1));
				Double damageModifier = config[2] * 1.0D;
				if (stamina.consume(event.getAmount() * damageModifier)) {
					event.setAmount(0);
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
	public void handleDamagePassive(LivingDamageEvent event) {
		if (event.getEntity().world.isRemote) return;
		
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			
			if (playerData.skillData.isSkillLearned("highground")) {
				if (player.getPosition().getY() - event.getEntity().getPosition().getY() > 1 && player.onGround) {
					event.setAmount(event.getAmount() + ((event.getAmount() / 100.0F) * Config.highgroundDamage));
				}
			}
			
			if (playerData.skillData.isSkillLearned("onedge")) {
				IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
				if (stamina.getStamina(EnumStaminaType.CURRENT) < 10) {
					event.setAmount(event.getAmount() + ((event.getAmount() / 100.0F) * Config.onedgeDamage));
				}
			}
		}		
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void handleDodgingPassive(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntity().world.isRemote)
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.capabilities.isCreativeMode) {
			// Creative mode - god mode
			return;
		}
		
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		
		if (playerData.skillData.isSkillLearned("dodging")) {
			float randNum = player.world.rand.nextFloat() * 100;
			if (randNum < Config.dodgingChance) {
				if (playerData.skillData.isSkillLearned("dodgingbuff")) {
					player.addPotionEffect(new PotionEffect(SpellPotion.getVanillaPotion("absorption"), 5 * 20));
				}
				event.setAmount(0);
				event.setCanceled(true);
			}
		}
	}

	public static void updatePlayerFlySpeed(EntityPlayer player, float mod) {
		if (player.world.isRemote)
			return;
			
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);

		if (playerData.race == EnumRace.PEGASUS) {
			float flySpeedMod = 0.0F;
			if (playerData.skillData.isSkillLearned("flySpeed")) {
				int lvl = playerData.skillData.getSkillLevel("flySpeed");
				flySpeedMod = lvl / 100.0F + mod;
			}

			PonyMagic.channel.sendTo(new FlySpeedPacket(flySpeedMod), (EntityPlayerMP) player);
		}
	}

	public static void updatePlayerMaxStamina(EntityPlayer player) {
		if (player.world.isRemote)
			return;

		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		if (playerData.skillData.isSkillLearned("staminaPool")) {
			int lvl = playerData.skillData.getSkillLevel("staminaPool");
			stamina.set(EnumStaminaType.MAXIMUM, (double) ((lvl + 2) * 50));
		} else {
			stamina.set(EnumStaminaType.MAXIMUM, 100.0D);  // Default stamina
		}
		stamina.sync((EntityPlayerMP) player);
	}

}
