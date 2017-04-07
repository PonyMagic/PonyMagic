package net.braunly.ponymagic.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.braunly.ponymagic.exp.PlaceBlock;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class PlaceEventHandler {
	
	public PlaceEventHandler() {}
	
	@SubscribeEvent
	public void placeBlock(PlaceEvent event) {
		PlaceBlock.process(event.block, event.player);
	}

}
