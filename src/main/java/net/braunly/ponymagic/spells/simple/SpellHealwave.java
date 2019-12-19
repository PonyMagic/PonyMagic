package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class SpellHealwave extends NamedSpell {

	public SpellHealwave(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		Integer[] config = Config.spells.get(getSpellName());
		double staminaAmount = stamina.getStamina(EnumStaminaType.CURRENT) / config[0];
		if (!playerData.getTickData().isTicking(getSpellName()) && stamina.consume(staminaAmount)) {
			Iterable<EntityPlayer> entities = player.world.getEntitiesWithinAABB(EntityPlayer.class,
					player.getEntityBoundingBox().expand(config[2], config[2], config[2]));
			entities.forEach(e -> e.heal((float) staminaAmount / config[1]));
			stamina.sync((EntityPlayerMP) player);
			playerData.getTickData().startTicking(getSpellName(), config[3]);
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
			return true;
		}
		return false;
	}

}
