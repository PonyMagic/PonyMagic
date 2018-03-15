package net.braunly.ponymagic.spells.simple;

import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SpellUnEnchant extends NamedSpell {

	public SpellUnEnchant(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		ItemStack item = player.getHeldItemMainhand();
		if (item != null) {
			IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
			if (stamina.consume((double) Config.spells.get(getSpellName())[0])) {
				NBTTagCompound tagCompound = item.getTagCompound();
				if (tagCompound != null && tagCompound.hasKey("ench")) {
					tagCompound.removeTag("ench");
					item.setRepairCost(0);
					stamina.sync((EntityPlayerMP) player);
					return true;
				}
			}
		}
		return false;
	}
}