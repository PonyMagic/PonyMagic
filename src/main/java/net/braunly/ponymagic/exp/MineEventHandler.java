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
		// process(event.getPlayer(),
		// event.getState().getBlock().getUnlocalizedName().substring(5));
		process(event.getPlayer(), event.getState().getBlock().getRegistryName().getResourcePath());

		// PonyMagic.log.info(event.getState().getBlock().getUnlocalizedName());
		// process(event.getPlayer(),
		// Integer.toString(Block.getIdFromBlock(event.getState().getBlock())));
	}

	@Override
	public ImmutableMap<String, Double> getExperienceTable() {
		return ExperienceStorage.getInstance().getMine();
	}

}
