package net.braunly.ponymagic.entity;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEntity {
    public static EntityEntry entityPortal;

    public static void init() {
        entityPortal = EntityEntryBuilder.create().entity(EntityPortal.class)
                .id(new ResourceLocation(PonyMagic.MODID, "portal"), 0)
                .name("portal").tracker(32, 2, false)
                .build();
    }

    public static void register(IForgeRegistry<EntityEntry> registry) {
        registry.registerAll(
                entityPortal
        );
    }
}
