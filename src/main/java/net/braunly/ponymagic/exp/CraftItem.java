package net.braunly.ponymagic.exp;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftItem extends Exp {
	public static HashMap<Integer, Double> exp = new HashMap<Integer, Double>();
	
	public static void process(ItemStack item, EntityPlayer player) {
		double expCount = exp.get(Item.getIdFromItem(item.getItem()));
		addExp(player, expCount);
	}
}
