package net.braunly.ponymagic;

import me.braunly.ponymagic.api.PonyMagicAPI;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = PonyMagic.MODID, useMetadata = true)
public class PonyMagic {
	public static final String MODID = "ponymagic";
	public static final String CLIENTPROXY = "net.braunly.ponymagic.proxy.ClientProxy";
	public static final String COMMONPROXY = "net.braunly.ponymagic.proxy.CommonProxy";

	public static final int MAX_LVL = 30;
	public static final PonyMagicCreativeTab creativeTab = new PonyMagicCreativeTab();
	
	@Instance(MODID)
	public static PonyMagic instance;
	@SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
	public static CommonProxy proxy;
	public static Logger log = LogManager.getLogger(MODID);
	public static SimpleNetworkWrapper channel;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PonyMagic.log.info("Initializing...");
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void setAboutToStart(FMLServerAboutToStartEvent event) {
		PonyMagicAPI.playerDataController = new PlayerDataController(event.getServer());
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}

}
