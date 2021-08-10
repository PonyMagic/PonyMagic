package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.config.PortalConfig;
import net.braunly.ponymagic.entity.EntityPortal;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class SpellPortal extends NamedSpell {
    public SpellPortal(String spellName) {
        super(spellName);
    }

    @Override
    public boolean cast(EntityPlayer player, Skill skillConfig, String[] args) {
        if (args.length != 2) return  false;

        IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
        String portalName = args[1];
        String configPortalName = String.format(
                "%s#%s#%s",
                player.world.getWorldInfo().getWorldName(),
                player.world.provider.getDimension(),
                portalName
        );
        BlockPos target = PortalConfig.getPortal(configPortalName);
        if (target != null && stamina.consume((double) skillConfig.getStamina())) {
            player.world.spawnEntity(new EntityPortal(player.world, player.posX, player.posY, player.posZ, target, portalName));
            stamina.sync((EntityPlayerMP) player);
            return true;
        }
        return false;
    }

    @Override
    public boolean cast(EntityPlayer player, Skill skillConfig) {
        return false;
    }
}
