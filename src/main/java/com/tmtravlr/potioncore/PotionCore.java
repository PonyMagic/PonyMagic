package com.tmtravlr.potioncore;

import com.tmtravlr.potioncore.network.CToSMessage;
import com.tmtravlr.potioncore.network.PacketHandlerClient;
import com.tmtravlr.potioncore.network.PacketHandlerServer;
import com.tmtravlr.potioncore.network.SToCMessage;
import com.tmtravlr.potioncore.potion.EntityPotionCorePotion;
import com.tmtravlr.potioncore.potion.ItemPotionCorePotion;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

/**
 * Potion mod!
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
@Mod(modid = PotionCore.MOD_ID, version = PotionCore.VERSION, name = PotionCore.MOD_NAME)
public class PotionCore
{
    public static final String MOD_ID = "potioncore";
    public static final String VERSION = "@VERSION@";
    public static final String MOD_NAME = "Potion Core";
	
	@Mod.Instance(PotionCore.MOD_ID)
	public static PotionCore instance;
	
	@SidedProxy(clientSide="com.tmtravlr.potioncore.ClientProxy", serverSide="com.tmtravlr.potioncore.CommonProxy")
	public static CommonProxy proxy;
    
	public static SimpleNetworkWrapper networkWrapper;
	
	public static CreativeTabs tabPotionCore = new CreativeTabs(MOD_ID) {
		@SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return ItemPotionCorePotion.instance;
        }
	};
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		ConfigLoader.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigLoader.load();
        PotionCoreHelper.increasePotionTypesSize();
        PotionCoreEffects.loadPotionEffects();
        proxy.loadInverted();
        
        ItemPotionCorePotion.instance = new ItemPotionCorePotion();
        GameRegistry.registerItem(ItemPotionCorePotion.instance, "custom_potion");
        
        proxy.registerEventHandlers();
        
		//Register packet system
		
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		networkWrapper.registerMessage(PacketHandlerServer.class, CToSMessage.class, 0, Side.SERVER);
//		if(event.getSide() == Side.CLIENT) {
			networkWrapper.registerMessage(PacketHandlerClient.class, SToCMessage.class, 1, Side.CLIENT);
//		}

    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.registerRenderers();
    	EntityRegistry.registerModEntity(EntityPotionCorePotion.class, "CustomPotion", 0, this, 32, 5, true);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    	//Get good and bad potion effects
		for(int i = 0; i < Potion.potionTypes.length; i++) {
			if(Potion.potionTypes[i] != null) {
				PotionCoreHelper.potions.put(Potion.potionTypes[i].getName(), Potion.potionTypes[i]);
				if(PotionCoreHelper.isBadEffect(Potion.potionTypes[i])) {
					PotionCoreHelper.badEffectList.add(Potion.potionTypes[i]);
				}
				else {
					PotionCoreHelper.goodEffectList.add(Potion.potionTypes[i]);
				}
			}
		}
        
		PotionCoreHelper.loadInversions();
    }
    
}
