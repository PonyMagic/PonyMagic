package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.PonyMagicModPermissions;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.braunly.ponymagic.spells.SpellShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;

import com.tmtravlr.potioncore.PotionCoreHelper;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class StaminaShieldHandler 
{	
	public StaminaShieldHandler() {	}

	@SubscribeEvent
	public void handlePlayerHurt(LivingHurtEvent event) 
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
}
