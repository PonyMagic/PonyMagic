package net.braunly.ponymagic.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.braunly.ponymagic.exp.CraftItem;

public class CraftEventHandler {
	
	public CraftEventHandler() {}
	
	@SubscribeEvent
	public void craftItem(ItemCraftedEvent event) {
		CraftItem.process(event.crafting, event.player);		
	}
	
	@SubscribeEvent
	public void smeltItem(ItemSmeltedEvent event) {
		CraftItem.process(event.smelting, event.player);
	}

}
