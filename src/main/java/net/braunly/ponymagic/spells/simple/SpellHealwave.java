package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class SpellHealwave extends NamedSpell {

	public SpellHealwave(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		double staminaAmount = stamina.getStamina(EnumStaminaType.CURRENT) * skillConfig.getStamina() / 100;
		if (!playerData.getTickData().isTicking(getSpellName()) && stamina.consume(staminaAmount)) {
			int radius = skillConfig.getSpellData().get("radius");
			Iterable<EntityPlayer> entities = player.world.getEntitiesWithinAABB(EntityPlayerMP.class,
					player.getEntityBoundingBox().grow(radius, radius, radius));
			entities.forEach(e -> e.heal((float) (staminaAmount * skillConfig.getSpellData().get("heal_percent") / 100)));
			stamina.sync((EntityPlayerMP) player);
			playerData.getTickData().startTicking(getSpellName(), skillConfig.getSpellData().get("cooldown"));
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
			return true;
		}
		return false;
	}

}
