package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathEventHandler extends ExperienceHandler {

	public DeathEventHandler() {
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void death(LivingDeathEvent event) {
		Entity damageSource = event.getSource().getTrueSource();
		if (damageSource instanceof EntityPlayer) {
			process((EntityPlayer) damageSource, event.getEntity().getClass().getSimpleName());
		} else if (damageSource instanceof EntityArrow && ((EntityArrow) damageSource).shootingEntity instanceof EntityPlayer) {
			process((EntityPlayer) ((EntityArrow) damageSource).shootingEntity, event.getEntity().getClass().getSimpleName());
		}
	}

	@Override
	public ImmutableMap<String, Double> getExperienceTable() {
		return ExperienceStorage.getInstance().getKill();
	}

}