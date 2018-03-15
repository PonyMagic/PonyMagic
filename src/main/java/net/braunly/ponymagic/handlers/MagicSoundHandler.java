package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MagicSoundHandler {

	public static SoundEvent LEVEL_UP;

	public static void init() {
		LEVEL_UP = createSoundEvent("levelUp");
	}

	private static SoundEvent createSoundEvent(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(PonyMagic.MODID, soundName);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}
}