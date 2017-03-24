package net.braunly.ponymagic.exp;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

public class PlaceBlock extends Exp {
	public static HashMap<Integer, Double> exp = new HashMap<Integer, Double>();
	
	public static void process(Block block, EntityPlayer player) {
		double expCount = exp.get(Block.getIdFromBlock(block));
		addExp(player, expCount);
	}
}
