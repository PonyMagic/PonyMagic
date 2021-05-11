package net.braunly.ponymagic.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	public static final ItemResetBook RESET_BOOK = new ItemResetBook("reset_book");
	public static final ItemCloudInBottle CLOUD_IN_BOTTLE = new ItemCloudInBottle("cloud_in_bottle");

	public static final ItemSpellBook PORTAL_SPELL_BOOK = new ItemSpellBook("spell_book", "portal");

	private ModItems() {
		throw new IllegalStateException("Utility class");
	}

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				RESET_BOOK,
				CLOUD_IN_BOTTLE,
				PORTAL_SPELL_BOOK
		);
	}
	
	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(RESET_BOOK, 0,
				new ModelResourceLocation(RESET_BOOK.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(CLOUD_IN_BOTTLE, 0,
				new ModelResourceLocation(CLOUD_IN_BOTTLE.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(PORTAL_SPELL_BOOK, 0,
				new ModelResourceLocation(PORTAL_SPELL_BOOK.getRegistryName(), "inventory"));
	}
}
