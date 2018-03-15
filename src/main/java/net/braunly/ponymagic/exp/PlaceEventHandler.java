package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlaceEventHandler extends ExperienceHandler {

	public PlaceEventHandler() {
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void placeBlock(PlaceEvent event) {
		// process(event.getPlayer(),
		// event.getState().getBlock().getUnlocalizedName().substring(5));
		process(event.getPlayer(), event.getState().getBlock().getRegistryName().getResourcePath());
		// PonyMagic.log.info(event.getPlacedBlock().getBlock().getUnlocalizedName());

		// process(event.getPlayer(),
		// Integer.toString(Block.getIdFromBlock(event.getPlacedBlock().getBlock())));
	}

	@Override
	public ImmutableMap<String, Double> getExperienceTable() {
		return ExperienceStorage.getInstance().getPlace();
	}

}
