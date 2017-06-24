package net.braunly.ponymagic;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindings {
	public static KeyBinding skills_gui;
	
	public static void init() {
		skills_gui = new KeyBinding("key.skills", Keyboard.KEY_O, "key.categories.ponymagic");
        ClientRegistry.registerKeyBinding(skills_gui);
    }
}