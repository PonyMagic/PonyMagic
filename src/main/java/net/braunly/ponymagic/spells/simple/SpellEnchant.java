package net.braunly.ponymagic.spells.simple;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.NamedSpell;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.List;
import java.util.Random;

public class SpellEnchant extends NamedSpell {

	public SpellEnchant(String spellName) {
		super(spellName);
	}

	@Override
	public boolean cast(EntityPlayer player, Skill skillConfig) {
		ItemStack itemStack = player.getHeldItemMainhand();
		int enchLevel = skillConfig.getSpellData().get("enchantment_level");
		if (itemStack.isEmpty() ||
				!itemStack.isItemEnchantable() ||
				itemStack.getItem().getItemEnchantability() <= 0 ||
				player.experienceLevel < enchLevel) {
			return false;
		}
		Random rand = player.world.rand;
		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		if (stamina.consume((double) skillConfig.getStamina())) {
			List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, itemStack, enchLevel,
					false);
			boolean isHeldBook = itemStack.getItem() == Items.BOOK;

			player.addExperienceLevel(-1 * skillConfig.getSpellData().get("experience"));

			if (isHeldBook) {
				itemStack = new ItemStack(Items.ENCHANTED_BOOK, itemStack.getCount());
			}

			int j = isHeldBook && list.size() > 1 ? rand.nextInt(list.size()) : -1;

			for (int k = 0; k < list.size(); ++k)
			{
				EnchantmentData enchantmentdata = list.get(k);
				if (!isHeldBook || k != j) {
					if (isHeldBook) {
						ItemEnchantedBook.addEnchantment(itemStack, enchantmentdata);
						player.setHeldItem(EnumHand.MAIN_HAND, itemStack);
					} else {
						itemStack.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
					}
				}
			}
			stamina.sync((EntityPlayerMP) player);
			return true;
		}
		return false;
	}
}