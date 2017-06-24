package net.braunly.ponymagic.spells;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SpellUnEnchant extends Spell {
	
	public SpellUnEnchant(String spellName) {
		this.spellName = spellName;
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		ItemStack item = player.getHeldItem();
		if (item != null) {
			StaminaPlayer props = StaminaPlayer.get(player);
			if (props.remove(StaminaType.CURRENT, Config.spells.get(this.spellName)[0])) {
				item.stackTagCompound.removeTag("ench");
				item.setRepairCost(0);
				return true;
			}
		}
		return false;
	}
}