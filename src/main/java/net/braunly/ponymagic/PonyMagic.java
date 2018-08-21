package net.braunly.ponymagic;

import lombok.NonNull;
import com.codahale.metrics.MetricRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.proxy.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = PonyMagic.MODID, useMetadata = true)
public class PonyMagic {
	public static final String MODID = "ponymagic";
	public static final String CLIENTPROXY = "net.braunly.ponymagic.proxy.ClientProxy";
	public static final String COMMONPROXY = "net.braunly.ponymagic.proxy.CommonProxy";
	@NonNull public static final MetricRegistry METRICS = new MetricRegistry();

	public static final int MAX_LVL = 30;

	@Instance(MODID)
	public static PonyMagic instance;
	@SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
	public static CommonProxy proxy;
	public static Logger log = LogManager.getLogger(MODID);
	public static SimpleNetworkWrapper channel;

	public static MinecraftServer Server = null;

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
		Server = event.getServer();
		new PlayerDataController();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}

}
