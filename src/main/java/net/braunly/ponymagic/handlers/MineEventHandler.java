package net.braunly.ponymagic.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.braunly.ponymagic.exp.MineBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class MineEventHandler {
	
	public MineEventHandler() {}
	
	@SubscribeEvent
	public void mineBlock(BreakEvent event) {
		MineBlock.process(event.block, event.getPlayer());
	}

}
