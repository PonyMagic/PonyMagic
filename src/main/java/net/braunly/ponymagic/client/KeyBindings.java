package net.braunly.ponymagic.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding skills_gui;

	public static void init() {
		skills_gui = new KeyBinding("key.skills", Keyboard.KEY_O, "key.categories.ponymagic");
		ClientRegistry.registerKeyBinding(skills_gui);
	}
}