package net.braunly.ponymagic.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
	public static final KeyBinding skillsGui =
			new KeyBinding("key.skills", Keyboard.KEY_O, "key.categories.ponymagic");
	public static final KeyBinding questsGui =
			new KeyBinding("key.quests", Keyboard.KEY_P, "key.categories.ponymagic");

	private KeyBindings() {
		throw new IllegalStateException("Utility class");
	}

	public static void init() {
		ClientRegistry.registerKeyBinding(skillsGui);
		ClientRegistry.registerKeyBinding(questsGui);
	}
}