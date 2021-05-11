package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MagicSoundHandler {
	public static final SoundEvent LEVEL_UP = createSoundEvent("levelup");

	private MagicSoundHandler()  {
		throw new IllegalStateException("Utility class");
	}

	private static SoundEvent createSoundEvent(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(PonyMagic.MODID, soundName);
		return new SoundEvent(soundID);
	}
}