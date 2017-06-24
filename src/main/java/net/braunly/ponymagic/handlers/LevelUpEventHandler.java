package net.braunly.ponymagic.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.braunly.ponymagic.event.LevelUpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class LevelUpEventHandler {
	
	public LevelUpEventHandler() {};
	
	@SubscribeEvent
	public void levelUp(LevelUpEvent event) {		
		if (!(event.getPlayer() instanceof EntityPlayer)) return;
		
		EntityPlayer player = event.getPlayer();
		
		player.addChatMessage(new ChatComponentText("Теперь у тебя " + event.getLevel() + " уровень."));
	}
}
