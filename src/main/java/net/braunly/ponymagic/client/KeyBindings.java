package net.braunly.ponymagic.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
	public static KeyBinding skills_gui;
	public static KeyBinding quests_gui;

	public static void init() {
		skills_gui = new KeyBinding("key.skills", Keyboard.KEY_O, "key.categories.ponymagic");
		quests_gui = new KeyBinding("key.quests", Keyboard.KEY_P, "key.categories.ponymagic");
		ClientRegistry.registerKeyBinding(skills_gui);
		ClientRegistry.registerKeyBinding(quests_gui);
	}
}