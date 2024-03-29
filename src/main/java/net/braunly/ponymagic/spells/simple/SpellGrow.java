package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SpellGrow extends NamedSpell {

	public SpellGrow(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		if (stamina.consume((double) skillConfig.getStamina())) {
			int radius = skillConfig.getSpellData().get("radius");
			for (int ry = (int) player.posY - radius; ry < (int) player.posY + radius; ry++) {
				for (int rx = (int) player.posX - radius; rx < (int) player.posX + radius; rx++) {
					for (int rz = (int) player.posZ - radius; rz < (int) player.posZ + radius; rz++) {
						ItemDye.applyBonemeal(new ItemStack(Items.DYE, 27, 15), player.world, new BlockPos(rx, ry, rz));
					}
				}
			}
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}

}
