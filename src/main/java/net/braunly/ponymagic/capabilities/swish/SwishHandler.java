package net.braunly.ponymagic.capabilities.swish;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SwishHandler {

	private static final ResourceLocation SWISH = new ResourceLocation(PonyMagic.MODID, "swish");
	// private static final EnumFacing facingStub = EnumFacing.DOWN;

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		if (entity instanceof EntityPlayer)
			event.addCapability(SWISH, new SwishProvider());
	}

	// Save max stamina after death
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();

		if (player.world.isRemote)
			return;
		
		ISwishCapability swish = player.getCapability(SwishProvider.SWISH, null);
		ISwishCapability oldSwish = event.getOriginal().getCapability(SwishProvider.SWISH, null);
		
		swish.setCanSwish(oldSwish.canSwish());
	}
}