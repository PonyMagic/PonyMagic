package net.braunly.ponymagic.items;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.item.Item;

public class ItemBase extends Item {
	
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(PonyMagic.creativeTab);		
	}
}
