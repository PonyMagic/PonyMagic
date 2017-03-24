package net.braunly.ponymagic.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.braunly.ponymagic.exp.MineBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class MineEventHandler {
	
	public MineEventHandler() {}
	
	@SubscribeEvent
	public void mineBlock(BreakEvent event) {
		MineBlock.process(event.block, event.getPlayer());
	}

}
