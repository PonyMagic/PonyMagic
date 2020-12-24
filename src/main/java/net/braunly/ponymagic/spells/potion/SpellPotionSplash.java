package net.braunly.ponymagic.spells.potion;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

public class SpellPotionSplash extends SpellPotion {
	private int range;

	public SpellPotionSplash(String spellName, int range) {
		super(spellName);
		this.range = range;
	}

	public SpellPotionSplash(String spellName, int range, boolean vanillaBased) {
		super(spellName, vanillaBased);
		this.range = range;
	}

	@Override
	boolean action(EntityPlayer player, Skill skillConfig) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		if (stamina != null && stamina.consume((double) skillConfig.getStamina())) {
			Iterable<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
					player.getEntityBoundingBox().expand(range, range, range));
			entities.forEach(e -> e.addPotionEffect(new PotionEffect(
					getPotion(),
					skillConfig.getEffect().get("duration"),
					skillConfig.getEffect().get("level")
			)));
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}
}
