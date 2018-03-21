package net.braunly.ponymagic.spells.simple;

import java.util.List;
import java.util.Random;

import net.braunly.ponymagic.capabilities.stamina.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SpellEnchant extends NamedSpell {
	private final int enchLevel = 30;

	public SpellEnchant(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		ItemStack itemStack = player.getHeldItemMainhand();
		if (itemStack.isEmpty()) {
			return false;
		}
		Random rand = player.world.rand;
		IStaminaStorage stamina = player.getCapability(StaminaProvider.STAMINA, null);
		if (stamina.consume((double) Config.spells.get(getSpellName())[0])) {
			List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, itemStack, this.enchLevel,
					false);
			boolean isHeldBook = itemStack.getItem() == Items.BOOK;

			player.addExperienceLevel(-this.enchLevel);

			if (isHeldBook) {
				itemStack = new ItemStack(Items.ENCHANTED_BOOK, itemStack.getCount());
			}

			int j = isHeldBook && list.size() > 1 ? rand.nextInt(list.size()) : -1;

			for (int k = 0; k < list.size(); ++k)
			{
				EnchantmentData enchantmentdata = list.get(k);
				if (!isHeldBook || k != j) {
//					if (isHeldBook) {
//						itemStack.addEnchantment(itemStack, enchantmentdata);
//					} else {
//						itemStack.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
//					}
					itemStack.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
				}
			}
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}
}