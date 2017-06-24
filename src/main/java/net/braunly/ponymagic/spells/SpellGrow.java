package net.braunly.ponymagic.spells;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class SpellGrow extends Spell {
	
	public SpellGrow(String spellName) {
		this.spellName = spellName;
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		StaminaPlayer props = StaminaPlayer.get(player);
		if (props.remove(StaminaType.CURRENT, Config.spells.get(this.spellName)[0])) {
			for (int ry = (int) player.posY - 3; ry < (int) player.posY + 3; ry++) {
				for (int rx = (int) player.posX - 3; rx < (int) player.posX + 3; rx++) {
					for (int rz = (int) player.posZ- 3; rz < (int) player.posZ + 3; rz++) {
						ItemDye.applyBonemeal(new ItemStack(Items.dye, 27, 15), player.worldObj, rx, ry, rz, player);
					}
				}
			}
			return true;
		}
		return false;
	}

}
