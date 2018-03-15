package net.braunly.ponymagic.exp;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;

public class CraftEventHandler extends ExperienceHandler {

	public CraftEventHandler() {
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void craftItem(ItemCraftedEvent event) {
		if (event.player.world.isRemote)
			return;
		// PonyMagic.log.info(event.crafting.getUnlocalizedName());
		// process(event.player, event.crafting.getUnlocalizedName().substring(5));
		process(event.player, event.crafting.getItem().getRegistryName().getResourcePath());
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void smeltItem(ItemSmeltedEvent event) {
		// PonyMagic.log.info(event.smelting.getUnlocalizedName());
		// process(event.player, event.smelting.getUnlocalizedName().substring(5));
		process(event.player, event.smelting.getItem().getRegistryName().getResourcePath());
	}

	@Override
	public ImmutableMap<String, Double> getExperienceTable() {
		return ExperienceStorage.getInstance().getCraft();
	}
}
