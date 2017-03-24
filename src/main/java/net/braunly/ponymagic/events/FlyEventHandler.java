package net.braunly.ponymagic.events;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.PonyMagicModPermissions;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FlyEventHandler 
{
	private static final double EPS = 1e-5;
	private boolean fly;
	private boolean hasP;
	
	public FlyEventHandler() {	}

	@SubscribeEvent
	public void handlePlayerUpdate(LivingUpdateEvent event) 
	{
		if (!(event.entity instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.entity;
		StaminaPlayer props = StaminaPlayer.get(player);

		if (props != null && !player.capabilities.isCreativeMode) 
		{
			fly = props.getFly();
			hasP = PonyMagicModPermissions.hasPermission(player, PonyMagicModPermissions.PEGAS);
			if (hasP)
			{
				if (props.getStaminaValue(StaminaType.CURRENT) > Config.flyEnableValue && !fly) {
					props.setFly(true);
					player.capabilities.allowFlying = true;
					player.sendPlayerAbilities();
				}

				if (player.capabilities.isFlying) {
					PonyMagic.channel.sendToServer(new TotalStaminaPacket(props.getStaminaValue(StaminaType.CURRENT)));
				}
				if (props.getStaminaValue(StaminaType.CURRENT) < Config.flyDisableValue && fly) {
					props.setFly(false);
					player.fallDistance = 0;
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					player.sendPlayerAbilities();
				}
			}
			
			if (player.onGround) {
				if (player.getFoodStats().getFoodLevel() > Config.highFoodLevel){
					props.addToQueue(StaminaType.CURRENT, Config.regenerationValue);
				} else if (player.getFoodStats().getFoodLevel() > Config.lowFoodLevel){
					props.addToQueue(StaminaType.CURRENT, Config.lowFoodRegenValue);
				}
			} else if (player.isInWater() && player.getFoodStats().getFoodLevel() > Config.lowFoodLevel) {
				props.addToQueue(StaminaType.CURRENT, Config.waterRegenValue);
			}

			if (player.getFoodStats().getFoodLevel() <= Config.lowFoodLevel && Config.burnStaminaWhenHungry) {
				props.zero();
			}
			props.update();
		}
	}
	
	@SubscribeEvent
	public void livingDeath(LivingDeathEvent event){
		if (!(event.entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) event.entity;
		StaminaPlayer props = StaminaPlayer.get(player);
		
		if (props != null) 
		{
			props.setFly(false);
			props.update();
		}
	}
	
	@SubscribeEvent
	public void changeDim(PlayerChangedDimensionEvent event){
		StaminaPlayer props = StaminaPlayer.get(event.player);
		if (props != null) 
		{
			props.setFly(false);
			props.update();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void handlePlayerConstruction(EntityConstructing event) {
		if (!(event.entity instanceof EntityPlayer)) return;
		if (StaminaPlayer.get((EntityPlayer)event.entity) == null){
			StaminaPlayer.register((EntityPlayer)event.entity);
		}
	}
}
