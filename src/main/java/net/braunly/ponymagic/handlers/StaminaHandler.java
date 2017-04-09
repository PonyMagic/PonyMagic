package net.braunly.ponymagic.handlers;

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

public class StaminaHandler 
{
	private static final double EPS = 1e-5;
	private boolean fly;
	private boolean hasP;
	
	public StaminaHandler() {	}

	@SubscribeEvent
	public void handlePlayerUpdate(LivingUpdateEvent event) 
	{
		if (!(event.entity instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.entity;
		StaminaPlayer props = StaminaPlayer.get(player);

		if (props != null && !player.capabilities.isCreativeMode) 
		{			
			if (player.onGround) {
				if (player.getFoodStats().getFoodLevel() > Config.highFoodLevel){
					props.add(StaminaType.CURRENT, Config.defaultStaminaRegen);
				} else if (player.getFoodStats().getFoodLevel() > Config.lowFoodLevel){
					props.add(StaminaType.CURRENT, Config.lowFoodStaminaRegen);
				}
			} else if (player.isInWater() && player.getFoodStats().getFoodLevel() > Config.lowFoodLevel) {
				props.add(StaminaType.CURRENT, Config.waterStaminaRegen);
			}

			if (player.getFoodStats().getFoodLevel() <= Config.lowFoodLevel && Config.burnStaminaWhenHungry) {
				props.zero();
			}
			
//			PonyMagic.log.info((!player.worldObj.isRemote ? "SERVER " : "CLIENT ") + props.getStaminaValue(StaminaType.CURRENT) + " / " + props.getStaminaValue(StaminaType.MAXIMUM));
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
