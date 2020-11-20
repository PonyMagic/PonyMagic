package net.braunly.ponymagic.quests;

import net.braunly.ponymagic.quests.handlers.*;
import net.minecraftforge.common.MinecraftForge;

public class Quests {

    public static void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockPlaceEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityKillEventHandler());
        MinecraftForge.EVENT_BUS.register(new BreedAnimalEventHandler());
        MinecraftForge.EVENT_BUS.register(new CraftEventHandler());
        MinecraftForge.EVENT_BUS.register(new SmeltedEventHandler());
        MinecraftForge.EVENT_BUS.register(new RepairEventHandler());
        MinecraftForge.EVENT_BUS.register(new TameAnimalEventHandler());
        MinecraftForge.EVENT_BUS.register(new PickupEventHandler());
        MinecraftForge.EVENT_BUS.register(new ItemUseEventHandler());
        // FIXME: test this event
        //MinecraftForge.EVENT_BUS.register(new AchieveEventHandler());
    }
}
