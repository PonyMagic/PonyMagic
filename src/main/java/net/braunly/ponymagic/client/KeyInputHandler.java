package net.braunly.ponymagic.client;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.gui.GuiHandler;
import net.braunly.ponymagic.gui.GuiSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBindings.skills_gui.isPressed()) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			if (!FMLClientHandler.instance().isGUIOpen(GuiSkills.class)) {
				player.openGui(PonyMagic.instance, GuiHandler.SKILLS_GUI, player.world, (int) player.posX,
						(int) player.posY, (int) player.posZ);
			}
		}
	}
}
