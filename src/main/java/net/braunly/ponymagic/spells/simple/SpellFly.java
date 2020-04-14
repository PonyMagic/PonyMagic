package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class SpellFly extends NamedSpell {
    public SpellFly(String spellName) {
        super(spellName);
    }

    @Override
    public boolean cast(EntityPlayer player, Integer level) {
        IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        Integer[] config = Config.spells.get(getSpellName());
        if (!playerData.getTickData().isTicking(getSpellName()) && stamina.consume((double) config[1])) {

            player.addPotionEffect(new PotionEffect(SpellPotion.getCustomPotion(this.getSpellName()), config[0], config[2]));

            stamina.sync((EntityPlayerMP) player);
            playerData.getTickData().startTicking(getSpellName(), config[3]);
            PonyMagicAPI.playerDataController.savePlayerData(playerData);
            return true;
        }
        return false;
    }

    private boolean teleportTo(EntityPlayer player, double x, double y, double z) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(player, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        double d0 = player.posX;
        double d1 = player.posY;
        double d2 = player.posZ;
        player.posX = event.getTargetX();
        player.posY = event.getTargetY();
        player.posZ = event.getTargetZ();
        boolean flag = false;
        BlockPos blockpos = new BlockPos(player.posX, player.posY, player.posZ);

        if (player.world.isBlockLoaded(blockpos))
        {
            player.setPositionAndUpdate(player.posX, player.posY, player.posZ);

            if (player.world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty())
            {
                flag = true;
            }
        }

        if (!flag)
        {
            player.setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        else
        {
            if(player.world instanceof WorldServer) {

                ((WorldServer)player.world).spawnParticle(EnumParticleTypes.PORTAL, true, player.posX, player.posY, player.posZ, 128, 1, 2, 1, 0, new int[0]);
            }

            player.world.playSound((EntityPlayer)null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, player.getSoundCategory(), 1.0F, 1.0F);
            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, player.getSoundCategory(), 1.0F, 1.0F);
            return true;
        }
    }

}
