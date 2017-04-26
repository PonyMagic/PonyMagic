package net.braunly.ponymagic.spells;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.network.packets.FlySpeedPacket;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellSpeed extends Spell {
	
	private int potionId = 1;
	
	public SpellSpeed(String potionName) {
		this.spellName = potionName;
	}
	
	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		// Check for cast flood.
//		if (player.isPotionActive(this.potionId)) return false;
		
		StaminaPlayer props = StaminaPlayer.get(player);
		Integer conf[] = Config.potions.get(this.spellName + "#" + level);
		if (props.remove(StaminaType.CURRENT, conf[1])) {
			int dur = conf[0] * 20;
			int lvl = conf[2] - 1;
			player.addPotionEffect(new PotionEffect(this.potionId, dur, lvl));
			
			float flySpeedMod = (float) level/80.0F;
			
			PonyMagic.proxy.setPlayerFlySpeed(player, flySpeedMod);
			
			return true;
		}
		return false;
	}
}
