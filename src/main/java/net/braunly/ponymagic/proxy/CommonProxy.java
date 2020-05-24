package net.braunly.ponymagic.proxy;

import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.stamina.StaminaHandler;
import net.braunly.ponymagic.capabilities.stamina.StaminaSerializer;
import net.braunly.ponymagic.capabilities.stamina.StaminaStorage;
import net.braunly.ponymagic.capabilities.swish.ISwishCapability;
import net.braunly.ponymagic.capabilities.swish.SwishHandler;
import net.braunly.ponymagic.capabilities.swish.SwishSerializer;
import net.braunly.ponymagic.capabilities.swish.SwishStorage;
import net.braunly.ponymagic.command.CommandCast;
import net.braunly.ponymagic.command.CommandMagic;
import net.braunly.ponymagic.command.CommandStamina;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.config.LevelConfig;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataHandler;
import net.braunly.ponymagic.data.PlayerDataSerializer;
import net.braunly.ponymagic.quests.handlers.BlockBreakEventHandler;
import net.braunly.ponymagic.gui.GuiHandler;
import net.braunly.ponymagic.handlers.LevelUpEventHandler;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.handlers.MagicSoundHandler;
import net.braunly.ponymagic.items.ModItems;
import net.braunly.ponymagic.network.packets.*;
import net.braunly.ponymagic.potions.PotionShield;
import net.braunly.ponymagic.potions.PotionStaminaHealthRegen;
import net.braunly.ponymagic.quests.handlers.BlockPlaceEventHandler;
import net.braunly.ponymagic.quests.handlers.EntityKillEventHandler;
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

import static com.tmtravlr.potioncore.PotionCoreEffects.POTIONS;

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
		PonyMagic.channel.registerMessage(SwishPacket.class, SwishPacket.class, 7, Side.CLIENT);

		// Main config
		Config.load(event.getSuggestedConfigurationFile());
		// Leveling configs
		LevelConfig.init(event.getModConfigurationDirectory());
		// Skills configs
		SkillConfig.init(event.getModConfigurationDirectory());
		PonyMagic.log.info("Configs loaded!");

		// Inject custom potions
		PonyMagic.log.info("Injecting custom potions...");
		injectPotions();


		// Register capability data
		CapabilityManager.INSTANCE.register(IStaminaStorage.class, new StaminaSerializer(), StaminaStorage.class);
		CapabilityManager.INSTANCE.register(ISwishCapability.class, new SwishSerializer(), SwishStorage.class);
		CapabilityManager.INSTANCE.register(IPlayerDataStorage.class, new PlayerDataSerializer(), PlayerData.class);
		
		ModItems.init();

		// Build table with experience for next level
		for (int i = 0; i <= PonyMagic.MAX_LVL; i++) {
			double prevExp = i == 0 ? 0.0D : PonyMagic.EXP_FOR_LVL.get(i-1);
			PonyMagic.EXP_FOR_LVL.put(i, prevExp + i * Config.expPerLevel);
			//PonyMagic.log.info("Level: " + i + " " + "Exp: " + PonyMagic.EXP_FOR_LVL.get(i));
		}
	}

	public void init(FMLInitializationEvent event) {
		PonyMagic.log.info("Handlers registration...");
		// Experience handlers
		MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
		MinecraftForge.EVENT_BUS.register(new BlockPlaceEventHandler());
		MinecraftForge.EVENT_BUS.register(new EntityKillEventHandler());

		MinecraftForge.EVENT_BUS.register(new MagicHandlersContainer());

		MinecraftForge.EVENT_BUS.register(new LevelUpEventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(PonyMagic.instance, new GuiHandler());

		// Attach capabilities to player
		MinecraftForge.EVENT_BUS.register(new StaminaHandler());
		MinecraftForge.EVENT_BUS.register(new SwishHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());

		MagicSoundHandler.init();
	}

	public void postInit(FMLPostInitializationEvent event) {
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
