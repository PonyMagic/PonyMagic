package net.braunly.ponymagic.spells.potion;

import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.config.Config;
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
	boolean action(EntityPlayer player, Integer level) {
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		Integer[] config = Config.potions.get(String.format("%s#%d", getSpellName(), level));
		if (stamina.consume((double) config[1])) {
			Iterable<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
					player.getEntityBoundingBox().expand(range, range, range));
			entities.forEach(e -> e.addPotionEffect(new PotionEffect(getPotion(), config[0] * TPS, config[2])));
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}
}