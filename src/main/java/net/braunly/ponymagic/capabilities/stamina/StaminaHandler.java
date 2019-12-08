package net.braunly.ponymagic.capabilities.stamina;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
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
		
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		IStaminaStorage oldStamina = PonyMagicAPI.getStaminaStorage(event.getOriginal());
		
		stamina.set(EnumStaminaType.MAXIMUM, oldStamina.getStamina(EnumStaminaType.MAXIMUM));
		stamina.sync((EntityPlayerMP) player);
	}
}