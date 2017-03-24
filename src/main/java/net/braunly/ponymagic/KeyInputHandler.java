package net.braunly.ponymagic;

import cpw.mods.fml.common.eventhandler.SubscribeEvent; 
import cpw.mods.fml.common.gameevent.InputEvent;


public class KeyInputHandler {

   @SubscribeEvent
   public void onKeyInput(InputEvent.KeyInputEvent event) {
       if(KeyBindings.skills.isPressed())
           System.out.println("SKILLS GUI");
   }
}
