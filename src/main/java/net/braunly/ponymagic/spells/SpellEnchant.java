package net.braunly.ponymagic.spells;

import java.util.List;
import java.util.Random;

import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SpellEnchant extends Spell {
	private int enchLevel = 30;
	private Random rand = new Random();
	
	public SpellEnchant(String spellName) {
		this.spellName = spellName;
	}

	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		ItemStack itemStack = player.getHeldItem();
		if (itemStack != null) {
			StaminaPlayer props = StaminaPlayer.get(player);
			if (props.remove(StaminaType.CURRENT, Config.spells.get(this.spellName)[0])) {
				List list = EnchantmentHelper.buildEnchantmentList(this.rand, itemStack, this.enchLevel);
                boolean flag = itemStack.getItem() == Items.book;

                if (list != null)
                {
                	player.addExperienceLevel(-this.enchLevel);

                    if (flag)
                    {
                    	itemStack.func_150996_a(Items.enchanted_book);
                    }

                    int j = flag && list.size() > 1 ? this.rand.nextInt(list.size()) : -1;

                    for (int k = 0; k < list.size(); ++k)
                    {
                        EnchantmentData enchantmentdata = (EnchantmentData)list.get(k);

                        if (!flag || k != j)
                        {
                            if (flag)
                            {
                                Items.enchanted_book.addEnchantment(itemStack, enchantmentdata);
                            }
                            else
                            {
                            	itemStack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                            }
                        }
                    }
                }
				return true;
			}
		}
		return false;
	}
}