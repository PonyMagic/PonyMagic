package net.braunly.ponymagic.data;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDataHandler {

	private static final ResourceLocation PLAYERDATA = new ResourceLocation(PonyMagic.MODID, "playerdata");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		if (entity instanceof EntityPlayer)
			event.addCapability(PLAYERDATA, new PlayerDataProvider());
	}
}
