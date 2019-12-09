package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class SpellUnEnchant extends NamedSpell {

	public SpellUnEnchant(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		ItemStack itemStack = player.getHeldItemMainhand();
		if (itemStack != null && (itemStack.isItemEnchanted() || itemStack.getItem() == Items.ENCHANTED_BOOK)) {
			IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
			if (stamina.consume((double) Config.spells.get(getSpellName())[0])) {
				boolean isHeldBook = itemStack.getItem() == Items.ENCHANTED_BOOK;
				
				if (isHeldBook) {
					itemStack = new ItemStack(Items.BOOK);
					player.setHeldItem(EnumHand.MAIN_HAND, itemStack);
					return true;
				} else {
					NBTTagCompound tagCompound = itemStack.getTagCompound();
					if (tagCompound != null && tagCompound.hasKey("ench")) {
						tagCompound.removeTag("ench");
						itemStack.setRepairCost(0);
						stamina.sync((EntityPlayerMP) player);
						return true;
					}
				}
			}
		}
		return false;
	}
}