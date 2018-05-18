package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MineEventHandler extends ExperienceHandler {

	public MineEventHandler() {
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void mineBlock(BreakEvent event) {
		process(event.getPlayer(), event.getState().getBlock().getRegistryName().getResourcePath());

		// PonyMagic.log.info(event.getState().getBlock().getUnlocalizedName());
	}

	@Override
	public ImmutableMap<String, Double> getExperienceTable() {
		return ExperienceStorage.getInstance().getMine();
	}

}
