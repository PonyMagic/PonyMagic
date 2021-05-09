package net.braunly.ponymagic.handlers;

import net.braunly.ponymagic.entity.EntityPortal;
import net.braunly.ponymagic.spells.simple.SpellBlink;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityPortalInteractHandler {
    public EntityPortalInteractHandler() {}

    // Teleport player after interact with EntityPortal
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerInteractWithPortal(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof EntityPortal) || event.getEntityPlayer().world.isRemote)
            return;
        BlockPos target = ((EntityPortal) event.getTarget()).getTarget();
        if (target != null) {
            SpellBlink.teleportTo(event.getEntityPlayer(), target.getX(), target.getY(), target.getZ());
        }
    }
}
