package net.braunly.ponymagic.spells;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellWorkbench extends Spell {
	
	public SpellWorkbench() {
		this.spellName = "workbench";
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		player.displayGUIWorkbench((int)player.posX, (int)player.posY, (int)player.posZ);
		return true;
	}
}
