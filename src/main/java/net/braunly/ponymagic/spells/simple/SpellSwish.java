package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.capabilities.swish.ISwishCapability;
import net.braunly.ponymagic.capabilities.swish.SwishProvider;
import net.braunly.ponymagic.network.packets.SwishPacket;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SpellSwish extends NamedSpell {

	public SpellSwish(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		ISwishCapability swish = player.getCapability(SwishProvider.SWISH, null);
		if (swish.canSwish() && stamina.getStamina(EnumStaminaType.CURRENT) < 10) {
			
			Vec3d lookVec = player.getLookVec();
			
			double x = player.posX + lookVec.x;
			double y = player.posY + lookVec.y;
			double z = player.posZ + lookVec.z;
			
			double d5 = player.posX - x;
	        double d7 = player.posY + (double)player.getEyeHeight() - y;
	        double d9 = player.posZ - z;
	        double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

	        if (d13 != 0.0D)
	        {
	            d5 = d5 / d13;
	            d7 = d7 / d13;
	            d9 = d9 / d13;
	            float f3 = 5.0F;
	            double d12 = player.getDistance(x, y, z) / (double)f3;
	            double d10 = 1.0D - d12;
	            PonyMagic.channel.sendTo(new SwishPacket(d5 + d10, d7 + d10, d9 + d10), (EntityPlayerMP) player);
	            player.fallDistance = 0;
	        }
			
			swish.setCanSwish(false);
			return true;
		}       
		return false;
	}
}