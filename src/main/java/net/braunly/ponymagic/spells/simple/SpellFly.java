package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.braunly.ponymagic.spells.potion.SpellPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

public class SpellFly extends NamedSpell {
    public SpellFly(String spellName) {
        super(spellName);
    }

    @Override
    public boolean cast(EntityPlayer player, Skill skillConfig) {
        IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        if (!playerData.getTickData().isTicking(getSpellName()) && stamina.consume((double) skillConfig.getStamina())) {

            player.addPotionEffect(new PotionEffect(
                    SpellPotion.getCustomPotion(this.getSpellName()),
                    skillConfig.getEffect().get("duration"),
                    skillConfig.getEffect().get("level")
            ));

            stamina.sync((EntityPlayerMP) player);
            playerData.getTickData().startTicking(getSpellName(), skillConfig.getSpellData().get("cooldown"));
            PonyMagicAPI.playerDataController.savePlayerData(playerData);
            return true;
        }
        return false;
    }
}
