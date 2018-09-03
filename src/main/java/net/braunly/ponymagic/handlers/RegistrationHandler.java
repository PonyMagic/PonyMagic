package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.items.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=PonyMagic.MODID)
public class RegistrationHandler {
	
//	@SubscribeEvent
//	public static void registerBlocks(RegistryEvent.Register<Block> event) {
//	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItems.register(event.getRegistry());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModItems.registerModels();
	}
}
