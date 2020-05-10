package net.braunly.ponymagic.spells.potion;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;

import static com.tmtravlr.potioncore.PotionCoreEffects.POTIONS;

public class SpellPotion extends NamedSpell {
	private boolean vanillaBased;

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
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		Integer[] config = Config.potions.get(String.format("%s#%d", getSpellName(), level));
		if (stamina != null && stamina.consume((double) config[1])) {
			player.addPotionEffect(new PotionEffect(getPotion(), config[0], config[2]));
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
