package net.braunly.ponymagic.capabilities.stamina;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StaminaHandler {

	private static final ResourceLocation STAMINA = new ResourceLocation(PonyMagic.MODID, "stamina");
	// private static final EnumFacing facingStub = EnumFacing.DOWN;

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		if (entity instanceof EntityPlayer)
			event.addCapability(STAMINA, new StaminaProvider());
	}

	// Save max stamina after death
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();

		if (player.world.isRemote)
			return;
		
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		IStaminaStorage oldStamina = event.getOriginal().getCapability(StaminaProvider.STAMINA, null);
		
		stamina.set(EnumStaminaType.MAXIMUM, oldStamina.getStamina(EnumStaminaType.MAXIMUM));
		stamina.sync((EntityPlayerMP) player);
	}
}