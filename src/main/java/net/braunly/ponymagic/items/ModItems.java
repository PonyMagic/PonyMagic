package net.braunly.ponymagic.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	public static ItemResetBook resetBook;
	public static ItemCloudInBottle cloudInBottle;
	
	public static void init() {
		resetBook = new ItemResetBook("reset_book");
		cloudInBottle = new ItemCloudInBottle("cloud_in_bottle");
	}

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				resetBook,
				cloudInBottle
		);
	}
	
	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(resetBook, 0, 
				new ModelResourceLocation(resetBook.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(cloudInBottle, 0, 
				new ModelResourceLocation(cloudInBottle.getRegistryName(), "inventory"));
	}
}
