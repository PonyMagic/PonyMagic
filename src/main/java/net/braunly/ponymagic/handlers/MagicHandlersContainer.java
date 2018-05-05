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
	private final Timer handlePlayerUpdateTimer = PonyMagic.METRICS.timer(
			name(MagicHandlersContainer.class, "handlePlayerUpdate")
	);

	public MagicHandlersContainer() {
	}

	@SubscribeEvent
	public void handlePlayerUpdate(PlayerTickEvent event) {
		final Timer.Context context = handlePlayerUpdateTimer.time();
		try {
		// if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		if (event.side == Side.CLIENT)
			return;

		EntityPlayer player = event.player;// getEntityLiving();
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);

		if (player.capabilities.isCreativeMode) {
			// Creative mode - god mode
			return;
		}

		// PonyMagic.log.info(event.side + ": " +
		// stamina.getStamina(EnumStaminaType.CURRENT) + "/" +
		// stamina.getStamina(EnumStaminaType.MAXIMUM));

		// Stamina regeneration
		if (stamina.getStamina(EnumStaminaType.CURRENT) < stamina.getStamina(EnumStaminaType.MAXIMUM)
				&& player.getFoodStats().getFoodLevel() > Config.lowFoodLevel
				&& (player.onGround || player.isInWater())) {
			Double staminaRegen = 0.0D;

			if (player.getFoodStats().getFoodLevel() > Config.highFoodLevel) {
				staminaRegen = Config.defaultStaminaRegen;
			} else {
				staminaRegen = Config.lowFoodStaminaRegen;
			}

			if (player.isInWater()) {
				staminaRegen = Config.waterStaminaRegen;
			}

			if (playerData.skillData.isSkillLearned("staminaRegen")) {
				int lvl = playerData.skillData.getSkillLevel("staminaRegen");
				staminaRegen += lvl / 20.0D; // TODO: config
			}

			stamina.add(staminaRegen);
			stamina.sync((EntityPlayerMP) player);
		}

		// Take all stamina on low food level
		if (player.getFoodStats().getFoodLevel() <= Config.lowFoodLevel && Config.burnStaminaWhenHungry) {
			stamina.zero();
			stamina.sync((EntityPlayerMP) player);
		}

		// Fly handling
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
					player.addExhaustion(Config.flyExhausting); // 0.016
				} else {
					player.fallDistance = 0;
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					player.sendPlayerAbilities();

					// Handle auto slowfall
					if (playerData.skillData.isSkillLearned("slowfallauto")) {
						Potion slowFall = SpellPotion.getCustomPotion("slowfall");
						Integer[] config = Config.potions.get(String.format("%s#%d", "slow_fall_auto", 1));
						player.addPotionEffect(new PotionEffect(slowFall, config[0] * SpellPotion.TPS, config[2]));
					}
				}
			}

			// Handle fly speed modification
			Potion speedPotion = getVanillaPotion("speed");

			// no inspection ConstantConditions
			if (player.isPotionActive(speedPotion) && player.getActivePotionEffect(speedPotion).getDuration() < 2) {
				updatePlayerFlySpeed(player, 0);
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
				if (!stamina.consume(event.getAmount() * damageModifier)) {
					stamina.zero();
					stamina.sync((EntityPlayerMP) player);
					player.removePotionEffect(shieldPotion);
				}
				event.setAmount(0);
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
