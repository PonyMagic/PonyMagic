package net.braunly.ponymagic.proxy;

import static com.tmtravlr.potioncore.PotionCoreEffects.POTIONS;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaHandler;
import net.braunly.ponymagic.capabilities.stamina.StaminaSerializer;
import net.braunly.ponymagic.capabilities.stamina.StaminaStorage;
import net.braunly.ponymagic.command.CommandCast;
import net.braunly.ponymagic.command.CommandMagic;
import net.braunly.ponymagic.command.CommandStamina;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataHandler;
import net.braunly.ponymagic.data.PlayerDataSerializer;
import net.braunly.ponymagic.exp.CraftEventHandler;
import net.braunly.ponymagic.exp.DeathEventHandler;
import net.braunly.ponymagic.exp.MineEventHandler;
import net.braunly.ponymagic.exp.PlaceEventHandler;
import net.braunly.ponymagic.gui.GuiHandler;
import net.braunly.ponymagic.handlers.LevelUpEventHandler;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.handlers.MagicSoundHandler;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.braunly.ponymagic.network.packets.LevelUpSoundPacket;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.network.packets.ResetPacket;
import net.braunly.ponymagic.network.packets.SkillUpPacket;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.braunly.ponymagic.potions.PotionShield;
import net.braunly.ponymagic.potions.PotionStaminaHealthRegen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		PonyMagic.channel = NetworkRegistry.INSTANCE.newSimpleChannel(PonyMagic.MODID);
		PonyMagic.channel.registerMessage(TotalStaminaPacket.class, TotalStaminaPacket.class, 0, Side.CLIENT);
		PonyMagic.channel.registerMessage(PlayerDataPacket.class, PlayerDataPacket.class, 1, Side.CLIENT);
		PonyMagic.channel.registerMessage(FlySpeedPacket.class, FlySpeedPacket.class, 2, Side.CLIENT);

		PonyMagic.channel.registerMessage(RequestPlayerDataPacket.class, RequestPlayerDataPacket.class, 3, Side.SERVER);
		PonyMagic.channel.registerMessage(SkillUpPacket.class, SkillUpPacket.class, 4, Side.SERVER);
		PonyMagic.channel.registerMessage(ResetPacket.class, ResetPacket.class, 5, Side.SERVER);

		PonyMagic.channel.registerMessage(LevelUpSoundPacket.class, LevelUpSoundPacket.class, 6, Side.CLIENT);

		Config.load(event.getSuggestedConfigurationFile());
		PonyMagic.log.info("Config loaded!");

		// Inject custom potions
		PonyMagic.log.info("Injecting custom potions...");
		injectPotions();


		// Register capability data
		CapabilityManager.INSTANCE.register(IStaminaStorage.class, new StaminaSerializer(), StaminaStorage.class);
		CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataSerializer(), PlayerData.class);
	}

	public void init(FMLInitializationEvent event) {
		PonyMagic.log.info("Handlers registration...");
		// Experience handlers
		MinecraftForge.EVENT_BUS.register(new MineEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlaceEventHandler());
		MinecraftForge.EVENT_BUS.register(new CraftEventHandler());
		MinecraftForge.EVENT_BUS.register(new DeathEventHandler());

		MinecraftForge.EVENT_BUS.register(new MagicHandlersContainer());

		MinecraftForge.EVENT_BUS.register(new LevelUpEventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(PonyMagic.instance, new GuiHandler());

		// Attach capabilities to player
		MinecraftForge.EVENT_BUS.register(new StaminaHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());

		MagicSoundHandler.init();
	}

	public void postInit(FMLPostInitializationEvent event) {
		if (Config.metricsEnabled) {
			initMetrics();
			PonyMagic.log.info("Initialized metrics");
		} else {
			PonyMagic.log.info("Metrics disabled");
		}
	}

	private void initMetrics() {
		final Graphite graphite = new Graphite(new InetSocketAddress(Config.graphiteHost, Config.graphitePort));
		final GraphiteReporter reporter = GraphiteReporter.forRegistry(PonyMagic.METRICS)
				.prefixedWith(Config.graphitePrefix)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MICROSECONDS)
				.filter(MetricFilter.ALL)
				.build(graphite);
		reporter.start(Config.reportInterval, TimeUnit.SECONDS);
	}

	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandCast());
		event.registerServerCommand(new CommandMagic());
		event.registerServerCommand(new CommandStamina());
	}

	private void injectPotions() {
		new PotionShield();
		new PotionStaminaHealthRegen();
		POTIONS.put(PotionShield.NAME, PotionShield.instance);
		POTIONS.put(PotionStaminaHealthRegen.NAME, PotionStaminaHealthRegen.instance);
	}

	public IThreadListener getListener(MessageContext ctx) {
		return (WorldServer) ctx.getServerHandler().player.world;
	}

	public EntityPlayer getPlayer(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	public void registerRenderers() {
		// TODO Auto-generated method stub

	}
}
