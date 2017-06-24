package net.braunly.ponymagic.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.race.EnumRace;
import net.braunly.ponymagic.spells.SpellShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class MagicHandlersContainer 
{	
	public MagicHandlersContainer() {	}

	@SubscribeEvent
	public void handlePlayerUpdate(LivingUpdateEvent event) 
	{
		if (!(event.entity instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.entity;
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		StaminaPlayer props = StaminaPlayer.get(player);

		if (props != null && !player.capabilities.isCreativeMode) 
		{
			// Stamina regenaration
			if (player.getFoodStats().getFoodLevel() > Config.lowFoodLevel) {
				float staminaRegen = 0;
				
				if (player.onGround && player.getFoodStats().getFoodLevel() > Config.highFoodLevel) {
					staminaRegen = Config.defaultStaminaRegen;
				} else if (player.isInWater()) {
					staminaRegen = Config.waterStaminaRegen;
				} else {
					staminaRegen = Config.lowFoodStaminaRegen;
				}
				
				if (playerData.skillData.isSkillLearned("staminaRegen")) {
					int lvl = playerData.skillData.getSkillLevel("staminaRegen");
					staminaRegen += (float) lvl/20.0F;
				}
				
				props.add(StaminaType.CURRENT, staminaRegen);
				
			}
			
			// Take all stamina on low food level
			if (player.getFoodStats().getFoodLevel() <= Config.lowFoodLevel && Config.burnStaminaWhenHungry) {
				props.zero();
			}
			
			// Fly handling
			if (playerData.race == EnumRace.PEGAS)
			{
				if (props.getStaminaValue(StaminaType.CURRENT) > 5) {
					player.capabilities.allowFlying = true;
					player.sendPlayerAbilities();
				}

				if (player.capabilities.isFlying) {
					float flySpendingValue = 0.0F;
					
					// Fly duration
					if (playerData.skillData.isSkillLearned("flydur")) {
						int lvl = playerData.skillData.getSkillLevel("flydur");
						flySpendingValue = Config.flySpendingValue - (float) lvl/50.0F;
					} else {
						flySpendingValue = Config.flySpendingValue;
					}
					
					if (props.remove(StaminaType.CURRENT, flySpendingValue)) {   // 0.8 stps
						player.addExhaustion(0.016F);  // FIXME в конфиг
					} else {
						player.fallDistance = 0;
						player.capabilities.isFlying = false;
						player.capabilities.allowFlying = false;
						player.sendPlayerAbilities();
					}
				}
			}

//			if (playerRace != null)
//				PonyMagic.log.info((!player.worldObj.isRemote ? "SERVER " : "CLIENT ") + playerRace.name());
//			PonyMagic.log.info((!player.worldObj.isRemote ? "SERVER " : "CLIENT ") + props.getStaminaValue(StaminaType.CURRENT) + " / " + props.getStaminaValue(StaminaType.MAXIMUM));
		}
	}
	
	@SubscribeEvent
	public void handleFlySpeedModification(LivingUpdateEvent event)
	{
		if (!(event.entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) event.entity;
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		
		if (playerData.race == EnumRace.PEGAS) {
			if (player.isPotionActive(Potion.moveSpeed.getId()) && player.getActivePotionEffect(Potion.moveSpeed).getDuration() < 2) {	
				PonyMagic.proxy.setPlayerFlySpeed(player, 0);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handlePegasusFlySpeed(PlayerLoggedInEvent event) {		
		PonyMagic.proxy.setPlayerFlySpeed(event.player, 0);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void handleMaximunStaminaValue(PlayerLoggedInEvent event) {
		PonyMagic.proxy.setPlayerMaxStamina(event.player);
	}
	
	@SubscribeEvent
	public void handleStaminaShield(LivingHurtEvent event) 
	{
		if (!(event.entityLiving instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.entityLiving;
		StaminaPlayer props = StaminaPlayer.get(player);

		if (props != null && !player.capabilities.isCreativeMode && player.isPotionActive(SpellShield.potionId)) 
		{			
			if (event.ammount > 0) {
				if (!props.remove(StaminaType.CURRENT, event.ammount * SpellShield.dmgModifier)) {
					props.zero();
					player.removePotionEffect(SpellShield.potionId);
				}
				event.ammount = 0;
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void handleStaminaPlayerConstructing(EntityConstructing event) {
		if (!(event.entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) event.entity;	
		if (StaminaPlayer.get(player) == null){
			StaminaPlayer.register(player);
		}
	}
}
