package net.braunly.ponymagic;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent; 
import cpw.mods.fml.common.gameevent.InputEvent;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.gui.GuiHandler;
import net.braunly.ponymagic.gui.GuiSkills;
import net.braunly.ponymagic.network.packets.OpenGuiPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;


public class KeyInputHandler {

   @SubscribeEvent
   public void onKeyInput(InputEvent.KeyInputEvent event) {
       if(KeyBindings.skills_gui.isPressed()) {
    	   if (!FMLClientHandler.instance().isGUIOpen(GuiSkills.class)) {
    		   EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//    		   EnumRace race = PlayerDataController.instance.getPlayerData(player).race;
//    		   if (race != null && race != EnumRace.REGULAR) {
    			   player.openGui(PonyMagic.instance, GuiHandler.SKILLS_GUI, player.worldObj,
    					   (int) player.posX, (int) player.posY, (int) player.posZ);
//    		   }
//    		   PonyMagic.channel.sendToServer(new OpenGuiPacket(GuiHandler.SKILLS_GUI));
    	   }
       }
   }
}
