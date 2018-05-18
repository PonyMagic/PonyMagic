package net.braunly.ponymagic.spells.simple;

import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.config.Config;
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
	public boolean cast(EntityPlayer player, Integer level) {
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		if (stamina.consume((double) Config.spells.get(getSpellName())[0])) {
			int radius = Config.spells.get(getSpellName())[1];
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
