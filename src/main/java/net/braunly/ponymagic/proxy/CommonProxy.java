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
import net.braunly.ponymagic.PonyMagicModPermissions;
import net.braunly.ponymagic.command.CommandCast;
import net.braunly.ponymagic.command.CommandMagic;
import net.braunly.ponymagic.command.CommandStamina;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.exp.Exp;
import net.braunly.ponymagic.handlers.CraftEventHandler;
import net.braunly.ponymagic.handlers.DeathEventHandler;
import net.braunly.ponymagic.handlers.StaminaHandler;
import net.braunly.ponymagic.handlers.MineEventHandler;
import net.braunly.ponymagic.handlers.PlaceEventHandler;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.braunly.ponymagic.spell.SpellAntidote;
import net.braunly.ponymagic.spell.SpellClimbing;
import net.braunly.ponymagic.spell.SpellDispel;
import net.braunly.ponymagic.spell.SpellDrowning;
import net.braunly.ponymagic.spell.SpellFireResistance;
import net.braunly.ponymagic.spell.SpellJump;
import net.braunly.ponymagic.spell.SpellNightVision;
import net.braunly.ponymagic.spell.SpellPurity;
import net.braunly.ponymagic.spell.SpellSlow;
import net.braunly.ponymagic.spell.SpellVulnerability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) 
    {
    	PonyMagic.channel = NetworkRegistry.INSTANCE.newSimpleChannel(PonyMagic.MODID);
    	PonyMagic.channel.registerMessage(TotalStaminaPacket.class, TotalStaminaPacket.class, 0, Side.CLIENT);
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
    	MinecraftForge.EVENT_BUS.register(new StaminaHandler());
    	MinecraftForge.EVENT_BUS.register(new DeathEventHandler());
    	
    	FMLCommonHandler.instance().bus().register(new KeyInputHandler());
    	KeyBindings.init(); 
    }

    public void postInit(FMLPostInitializationEvent event) 
    {
    	PonyMagic.log.info("Permissions initializing...");
		new PonyMagicModPermissions();
		
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
	
	private void initSpells() {
		PonyMagic.spells.put("antidote", new SpellAntidote());
		PonyMagic.spells.put("climbing", new SpellClimbing());
		PonyMagic.spells.put("jump", new SpellJump());
		PonyMagic.spells.put("dispel", new SpellDispel());
		PonyMagic.spells.put("fireresistance", new SpellFireResistance());
		PonyMagic.spells.put("drowning", new SpellDrowning());
		PonyMagic.spells.put("slow", new SpellSlow());
		PonyMagic.spells.put("purity", new SpellPurity());
		PonyMagic.spells.put("nightvision", new SpellNightVision());
		PonyMagic.spells.put("vulnerability", new SpellVulnerability());
	}
}