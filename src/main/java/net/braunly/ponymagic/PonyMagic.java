package net.braunly.ponymagic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.proxy.CommonProxy;

@Mod(modid = PonyMagic.MODID, version = PonyMagic.VERSION, useMetadata = true)
public class PonyMagic 
{
	public static final String MODID = "ponymagic";
    public static final String VERSION = "0.1";
	public static final String CLIENTPROXY = "net.braunly.ponymagic.proxy.ClientProxy";
	public static final String COMMONPROXY = "net.braunly.ponymagic.proxy.CommonProxy";
	
	public static final int MAX_LVL = 5;
	
	public static SimpleNetworkWrapper channel;
	
    @Instance(MODID)
	public static PonyMagic instance;
    
    @SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
    public static CommonProxy proxy;
    
    public static Logger log = LogManager.getLogger(MODID);
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	PonyMagic.log.info("Initializing...");
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
//    	proxy.registerRenderers();
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
    
    @EventHandler
    public void setAboutToStart(FMLServerAboutToStartEvent event) {
    	new PlayerDataController();
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
    	proxy.serverStarting(event);
    }

}
