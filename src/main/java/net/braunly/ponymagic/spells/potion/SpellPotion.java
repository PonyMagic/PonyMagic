package net.braunly.ponymagic.spells.potion;

import static com.tmtravlr.potioncore.PotionCoreEffects.POTIONS;

import javax.annotation.Nonnull;

import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellPotion extends NamedSpell {
	private boolean vanillaBased;
	public static final int TPS = 20;

	public SpellPotion(String spellName) {
		super(spellName);
		this.vanillaBased = false;
	}

	public SpellPotion(String spellName, boolean vanillaBased) {
		super(spellName);
		this.vanillaBased = vanillaBased;
	}

	@Nonnull
	public static Potion getCustomPotion(String potionName) {
		return POTIONS.get(potionName);
	}

	@Nonnull
	public static Potion getVanillaPotion(String potionName) {
		return Potion.getPotionFromResourceLocation(potionName);
	}

	@Nonnull
	Potion getPotion() {
		return vanillaBased ? getVanillaPotion(getSpellName()) : getCustomPotion(getSpellName());
	}

	boolean action(EntityPlayer player, Integer level) {
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		Integer[] config = Config.potions.get(String.format("%s#%d", getSpellName(), level));
		if (stamina.consume((double) config[1])) {
			player.addPotionEffect(new PotionEffect(getPotion(), config[0] * TPS, config[2]));
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		return action(player, level);
	}
}
