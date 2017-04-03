package net.braunly.ponymagic.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.braunly.ponymagic.exp.KillEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class DeathEventHandler {
	
	public DeathEventHandler() {}
	
	@SubscribeEvent
	public void death(LivingDeathEvent event) {
		if (event.source.getSourceOfDamage() instanceof EntityPlayer) {
			KillEntity.process(event.entity, (EntityPlayer) event.source.getSourceOfDamage());
		}
	}

}
