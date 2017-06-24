package net.braunly.ponymagic.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.braunly.ponymagic.KeyBindings;
import net.braunly.ponymagic.KeyInputHandler;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.command.CommandCast;
import net.braunly.ponymagic.command.CommandMagic;
import net.braunly.ponymagic.command.CommandStamina;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.exp.Exp;
import net.braunly.ponymagic.gui.GuiHandler;
import net.braunly.ponymagic.handlers.CraftEventHandler;
import net.braunly.ponymagic.handlers.DeathEventHandler;
import net.braunly.ponymagic.handlers.LevelUpEventHandler;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.handlers.MineEventHandler;
import net.braunly.ponymagic.handlers.PlaceEventHandler;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.network.packets.SkillUpPacket;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.braunly.ponymagic.spells.SpellEnchant;
import net.braunly.ponymagic.spells.SpellGrow;
import net.braunly.ponymagic.spells.SpellPotion;
import net.braunly.ponymagic.spells.SpellPotionSplash;
import net.braunly.ponymagic.spells.SpellShield;
import net.braunly.ponymagic.spells.SpellSpeed;
import net.braunly.ponymagic.spells.SpellUnEnchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) 
    {
    	PonyMagic.channel = NetworkRegistry.INSTANCE.newSimpleChannel(PonyMagic.MODID);
    	PonyMagic.channel.registerMessage(TotalStaminaPacket.class, TotalStaminaPacket.class, 0, Side.CLIENT);
    	PonyMagic.channel.registerMessage(PlayerDataPacket.class, PlayerDataPacket.class, 1, Side.CLIENT);
    	PonyMagic.channel.registerMessage(FlySpeedPacket.class, FlySpeedPacket.class, 2, Side.CLIENT);
    	
//    	PonyMagic.channel.registerMessage(OpenGuiPacket.class, OpenGuiPacket.class, 5, Side.SERVER);
    	PonyMagic.channel.registerMessage(RequestPlayerDataPacket.class, RequestPlayerDataPacket.class, 3, Side.SERVER);
    	PonyMagic.channel.registerMessage(SkillUpPacket.class, SkillUpPacket.class, 4, Side.SERVER);
    	
		Config.load(event.getSuggestedConfigurationFile());
    	PonyMagic.log.info("Config loaded!");
    }

    public void init(FMLInitializationEvent event) 
    {
    	PonyMagic.log.info("Handlers registration...");
    	this.registerRenderers();
    	MinecraftForge.EVENT_BUS.register(new MineEventHandler());
    	MinecraftForge.EVENT_BUS.register(new PlaceEventHandler());
    	FMLCommonHandler.instance().bus().register(new CraftEventHandler());
    	MinecraftForge.EVENT_BUS.register(new DeathEventHandler());
    	
    	MinecraftForge.EVENT_BUS.register(new MagicHandlersContainer());
    	FMLCommonHandler.instance().bus().register(new MagicHandlersContainer());
    	
    	MinecraftForge.EVENT_BUS.register(new LevelUpEventHandler());
    	
    	FMLCommonHandler.instance().bus().register(new KeyInputHandler());
    	KeyBindings.init(); 
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(PonyMagic.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) 
    {
		
    	PonyMagic.log.info("Experience loading...");
		new Exp().load();
		
		initSpells();
    }
    
	public void serverStarting(FMLServerStartingEvent event) 
	{
		event.registerServerCommand(new CommandCast());
		event.registerServerCommand(new CommandMagic());
    	event.registerServerCommand(new CommandStamina());
	}

	public void registerRenderers() {
		// TODO Auto-generated method stub
		
	}
	
	private void initSpells() {
		// Potions
		PonyMagic.spells.put("antidote", new SpellPotion("antidote"));
		PonyMagic.spells.put("climb", new SpellPotion("climb"));
		PonyMagic.spells.put("jump", new SpellPotion("jump", 8));
		PonyMagic.spells.put("dispel", new SpellPotion("dispel"));
		PonyMagic.spells.put("fireresistance", new SpellPotion("fireResistance", 12));
		PonyMagic.spells.put("drown", new SpellPotion("drown"));
		PonyMagic.spells.put("slow", new SpellPotionSplash("slow", 2, 2));
		PonyMagic.spells.put("purity", new SpellPotion("purity"));
		PonyMagic.spells.put("nightvision", new SpellPotion("nightVision", 16));
		PonyMagic.spells.put("vulnerable", new SpellPotion("vulnerable"));
		PonyMagic.spells.put("stepup", new SpellPotion("stepUp"));
		PonyMagic.spells.put("strength", new SpellPotion("strength", 5));
		PonyMagic.spells.put("haste", new SpellPotion("haste", 3));
		PonyMagic.spells.put("hpregen", new SpellPotion("hpRegen", 10));
		PonyMagic.spells.put("solidcore", new SpellPotion("solidCore"));
		PonyMagic.spells.put("tpbed", new SpellPotion("teleportSpawn"));
		PonyMagic.spells.put("heal", new SpellPotion("heal", 6));
		PonyMagic.spells.put("slowfall", new SpellPotion("slowFall"));
		
		// Spells
		PonyMagic.spells.put("grow", new SpellGrow("grow"));
		PonyMagic.spells.put("unenchant", new SpellUnEnchant("unenchant"));
		PonyMagic.spells.put("enchant", new SpellEnchant("enchant"));
		PonyMagic.spells.put("shield", new SpellShield("shield"));
		PonyMagic.spells.put("speed", new SpellSpeed("speedBoost"));
	}
	
	public void setPlayerFlySpeed(EntityPlayer player, float mod) {	
		if (player.worldObj.isRemote) return;
		
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		
		if (playerData.race == EnumRace.PEGAS && playerData.skillData.isSkillLearned("flySpeed")) {
			int lvl = playerData.skillData.getSkillLevel("flySpeed");
			float flySpeedMod = (float) lvl/100.0F + mod;
			
			PonyMagic.channel.sendTo(new FlySpeedPacket(flySpeedMod), (EntityPlayerMP) player);
		}
	}
	
	public void setPlayerMaxStamina(EntityPlayer player) {	
		if (player.worldObj.isRemote) return;
		
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		StaminaPlayer props = StaminaPlayer.get(player);
		if (playerData.skillData.isSkillLearned("staminaPool")) {
			int lvl = playerData.skillData.getSkillLevel("staminaPool");
			props.set(StaminaType.MAXIMUM, (lvl + 2)*50);
		}
	}
	
	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
	{
		switch(ctx.side)
        {
            case CLIENT:
            {
                assert false : "Message for CLIENT received on dedicated server";
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