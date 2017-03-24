package net.braunly.ponymagic.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.braunly.ponymagic.KeyBindings;
import net.braunly.ponymagic.gui.GuiStamina;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
	@Override
	public void registerRenderers() {
		MinecraftForge.EVENT_BUS.register(new GuiStamina(Minecraft.getMinecraft()));
	}
	
	@Override
    public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
    {
        switch(ctx.side)
        {
            case CLIENT:
            {
                EntityPlayer entityClientPlayerMP = Minecraft.getMinecraft().thePlayer;
                return entityClientPlayerMP;
            }
            case SERVER:
            {
                EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
                return entityPlayerMP;
            }
            default:
                assert false : "Invalid side in TestMsgHandler: " + ctx.side;
        }
        return null;
    }

}
