package net.braunly.ponymagic.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
	public static KeyBinding skillsGui;
	public static KeyBinding questsGui;

	public static void init() {
		skillsGui = new KeyBinding("key.skills", Keyboard.KEY_O, "key.categories.ponymagic");
		questsGui = new KeyBinding("key.quests", Keyboard.KEY_P, "key.categories.ponymagic");
		ClientRegistry.registerKeyBinding(skillsGui);
		ClientRegistry.registerKeyBinding(questsGui);
	}
}