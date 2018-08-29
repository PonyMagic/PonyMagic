package net.braunly.ponymagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PonyMagicCreativeTab extends CreativeTabs
{
	public PonyMagicCreativeTab() {
		super(PonyMagic.MODID);
//		setBackgroundImageName("item_search.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.ENCHANTED_BOOK);
	}
	
	 @Override
	 public boolean hasSearchBar() {
		 return true;
	 }

}