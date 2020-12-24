package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.MotionPacket;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class SpellDash extends NamedSpell {

	public SpellDash(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		if (stamina.consume((double) skillConfig.getStamina())) {

			double dashFactor = 1.5D;
			Vec3d lookVec = player.getLookVec().scale(dashFactor);
			PonyMagic.channel.sendTo(new MotionPacket(lookVec.x, lookVec.y, lookVec.z), (EntityPlayerMP) player);
			player.fallDistance = 0;
			return true;
		}       
		return false;
	}
}