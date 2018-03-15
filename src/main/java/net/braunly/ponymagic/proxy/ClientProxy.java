package net.braunly.ponymagic.proxy;

import net.braunly.ponymagic.client.KeyBindings;
import net.braunly.ponymagic.client.KeyInputHandler;
import net.braunly.ponymagic.client.RenderHandler;
import net.braunly.ponymagic.gui.GuiStamina;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		KeyBindings.init();
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void registerRenderers() {
		MinecraftForge.EVENT_BUS.register(new GuiStamina(Minecraft.getMinecraft()));
		MinecraftForge.EVENT_BUS.register(new RenderHandler());
	}

	@Override
	public IThreadListener getListener(MessageContext ctx) {
		return ctx.side == Side.CLIENT ? Minecraft.getMinecraft() : super.getListener(ctx);
	}

	@Override
	public EntityPlayer getPlayer(MessageContext ctx) {
		return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().player : super.getPlayer(ctx);
	}

}
