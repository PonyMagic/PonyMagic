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
        IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
        if (args.length == 2 && stamina.consume((double) skillConfig.getStamina())) {

            BlockPos target = PortalConfig.getPortal(args[1]);
            player.world.spawnEntity(new EntityPortal(player.world, player.posX, player.posY, player.posZ, target, args[1]));

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
