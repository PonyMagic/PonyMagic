package net.braunly.ponymagic.spells;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class SpellShield extends Spell {
	private Integer conf[];
	public static int potionId;
	public static int dmgModifier;
	
	public SpellShield(String spellName) {
		this.spellName = spellName;
		this.potionId = PotionCoreHelper.potions.get("potion." + this.spellName).getId();
		this.conf = Config.spells.get(this.spellName);
		this.dmgModifier = this.conf[2];
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		if (player.isPotionActive(potionId)) {
			player.removePotionEffect(this.potionId);
		} else {
			StaminaPlayer props = StaminaPlayer.get(player);
			if (props.remove(StaminaType.CURRENT, this.conf[1])) {
				player.addPotionEffect(new PotionEffect(potionId, this.conf[0] * 20, 0, true));  // ???
				return true;  // FIXME
			}
		}
		return false;
	}

}
