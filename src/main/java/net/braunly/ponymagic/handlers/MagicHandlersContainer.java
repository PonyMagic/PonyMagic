package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.stamina.EnumStaminaType;
import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.codahale.metrics.Timer;
import static com.codahale.metrics.MetricRegistry.name;
import static net.braunly.ponymagic.spells.potion.SpellPotion.getVanillaPotion;

public class MagicHandlersContainer {
	private final Timer handlePlayerUpdateTimer = PonyMagic.METRICS.timer(name(MagicHandlersContainer.class, "handlePlayerUpdate"));
	private final static Timer handlePlayerFlySpeedTimer = PonyMagic.METRICS.timer(name(MagicHandlersContainer.class, "handlePlayerFlySpeed"));
	private final Timer processStamina = PonyMagic.METRICS.timer(name(MagicHandlersContainer.class, "handlePlayerUpdate", "processStamina"));

	public MagicHandlersContainer() {
	}

	@SubscribeEvent
	public void handlePlayerUpdate(PlayerTickEvent event) {
		final Timer.Context context = handlePlayerUpdateTimer.time();
		try {

			if (event.side == Side.CLIENT)
				return;

			EntityPlayer player = event.player;// getEntityLiving();
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);

			if (player.capabilities.isCreativeMode || player.isSpectator() || playerData.race == EnumRace.REGULAR) {
				// Creatives, spectators and regulars not processing.
				return;
			}

			// Stamina regeneration
			final Timer.Context staminaContext = processStamina.time();
			try {
				
				Potion shieldPotion = SpellPotion.getCustomPotion("shield");						
				if (stamina.getStamina(EnumStaminaType.CURRENT) < stamina.getStamina(EnumStaminaType.MAXIMUM)
						&& player.getFoodStats().getFoodLevel() > Config.lowFoodLevel
						&& (player.onGround || player.isInWater())
						&& !player.isPotionActive(shieldPotion)) {
					Double staminaRegen = 0.0D;

					if (player.getFoodStats().getFoodLevel() > Config.highFoodLevel) {
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

					stamina.add(staminaRegen);
					stamina.sync((EntityPlayerMP) player);
				}

				// Take all stamina on low food level
				if (player.getFoodStats().getFoodLevel() <= Config.lowFoodLevel && Config.burnStaminaWhenHungry) {
					stamina.zero();
					stamina.sync((EntityPlayerMP) player);
				}
			} finally {
				staminaContext.stop();
			}

			if (playerData.race == EnumRace.PEGASUS) {
				if (stamina.getStamina(EnumStaminaType.CURRENT) > 5) {
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
							Potion slowFall = SpellPotion.getCustomPotion("slowfall");
							if (!player.isPotionActive(slowFall)) {
								Integer[] config = Config.potions.get(String.format("%s#%d", "slow_fall_auto", 1));
								player.addPotionEffect(new PotionEffect(slowFall, config[0] * SpellPotion.TPS, config[2]));
							}
						}
					}
				}
			}
		} finally {
			context.stop();
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

	@SubscribeEvent
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

	public static void updatePlayerFlySpeed(EntityPlayer player, float mod) {
		if (player.world.isRemote)
			return;

		final Timer.Context context = handlePlayerFlySpeedTimer.time();
		try {
			
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);

			if (playerData.race == EnumRace.PEGASUS) {
				float flySpeedMod = 0.0F;
				if (playerData.skillData.isSkillLearned("flySpeed")) {
					int lvl = playerData.skillData.getSkillLevel("flySpeed");
					flySpeedMod = lvl / 100.0F + mod;
				}

				PonyMagic.channel.sendTo(new FlySpeedPacket(flySpeedMod), (EntityPlayerMP) player);
			}
		} finally {
			context.stop();
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
