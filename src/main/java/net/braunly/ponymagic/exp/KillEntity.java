package net.braunly.ponymagic.exp;

import java.io.File;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;

public class KillEntity extends Exp {
	public static HashMap<String, Double> exp = new HashMap<String, Double>();
	
	public static void process(Entity entity, EntityPlayer player) {
		try {
			double expCount = exp.get(EntityList.getEntityString(entity));
			addExp(player, expCount);
		} catch (NullPointerException e) {
			
		}
	}
}
