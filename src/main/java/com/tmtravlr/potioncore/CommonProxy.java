package com.tmtravlr.potioncore;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public EntityPlayer getPlayer() {
		return null;
	}
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new PotionCoreEventHandler());
		FMLCommonHandler.instance().bus().register(new PotionCoreEventHandler());
	}
	
	public void registerRenderers() {}
	
	public void loadInverted() {}
	
	public void doPotionSmashEffects(double x, double y, double z, ItemStack stack) {}
	
}
