package com.tmtravlr.potioncore.potion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.PotionCoreEffects;
import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;
import com.tmtravlr.potioncore.PotionCoreHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionCorePotion extends ItemPotion {
	
	public static final int SPLASH_META = 16384;
	public static ItemPotionCorePotion instance;
	
	public ItemPotionCorePotion() {
		super();
		this.setUnlocalizedName("custom_potion").setCreativeTab(PotionCore.tabPotionCore).setTextureName("potion");
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (isSplash(stack.getItemDamage()))
        {
            if (!player.capabilities.isCreativeMode)
            {
                --stack.stackSize;
            }

            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityPotionCorePotion(world, player, stack.copy()));
            }

            return stack;
        }
        else
        {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
            return stack;
        }
    }
	
	public String getItemStackDisplayName(ItemStack potion)
    {
        String s = "";

        if (isSplash(potion.getItemDamage()))
        {
            s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
        }

        List list = Items.potionitem.getEffects(potion);
        String s1;

        if (list != null && !list.isEmpty())
        {
            s1 = ((PotionEffect)list.get(0)).getEffectName();
            s1 = s1 + ".postfix";
            return s + StatCollector.translateToLocal(s1).trim();
        }
        else
        {
            return super.getItemStackDisplayName(potion);
        }
        
    }
	
	/**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug)
    {
    	if(!(stack.hasTagCompound() && stack.getTagCompound().getBoolean("hideFlags"))) {
	        List list1 = Items.potionitem.getEffects(stack);
	        HashMultimap hashmultimap = HashMultimap.create();
	        Iterator iterator1;
	
	        if (list1 != null && !list1.isEmpty())
	        {
	            iterator1 = list1.iterator();
	
	            while (iterator1.hasNext())
	            {
	                PotionEffect potioneffect = (PotionEffect)iterator1.next();
	                String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
	                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
	                Map map = potion.func_111186_k();
	
	                if (map != null && map.size() > 0)
	                {
	                    Iterator iterator = map.entrySet().iterator();
	
	                    while (iterator.hasNext())
	                    {
	                        Entry entry = (Entry)iterator.next();
	                        AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
	                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
	                        hashmultimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
	                    }
	                }
	
	                if (potioneffect.getAmplifier() > 0)
	                {
	                    s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
	                }
	
	                if (potioneffect.getDuration() > 20)
	                {
	                    s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
	                }
	
	                if (potion.isBadEffect())
	                {
	                    list.add(EnumChatFormatting.RED + s1);
	                }
	                else
	                {
	                    list.add(EnumChatFormatting.GRAY + s1);
	                }
	            }
	        }
	        else
	        {
	            super.addInformation(stack, player, list, debug);
	        }
	
	        if (!hashmultimap.isEmpty())
	        {
	            list.add("");
	            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
	            iterator1 = hashmultimap.entries().iterator();
	
	            while (iterator1.hasNext())
	            {
	                Entry entry1 = (Entry)iterator1.next();
	                AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
	                double d0 = attributemodifier2.getAmount();
	                double d1;
	
	                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
	                {
	                    d1 = attributemodifier2.getAmount();
	                }
	                else
	                {
	                    d1 = attributemodifier2.getAmount() * 100.0D;
	                }
	
	                if (d0 > 0.0D)
	                {
	                    list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
	                }
	                else if (d0 < 0.0D)
	                {
	                    d1 *= -1.0D;
	                    list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
	                }
	            }
	        }
    	}
    }
	
    @SideOnly(Side.CLIENT)
    public boolean isEffectInstant(ItemStack stack)
    {
        List list = this.getEffects(stack);

        if (list != null && !list.isEmpty())
        {
            Iterator iterator = list.iterator();
            PotionEffect potioneffect;

            do
            {
                if (!iterator.hasNext())
                {
                    return false;
                }

                potioneffect = (PotionEffect)iterator.next();
            }
            while (!Potion.potionTypes[potioneffect.getPotionID()].isInstant());

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Render Pass sensitive version of hasEffect()
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return super.hasEffect(par1ItemStack) && pass == 0;
    }
	
	/**
	 * Returns the color of the potion depending on the effects in it
	 */
	@SideOnly(Side.CLIENT)
	@Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
		
		if(pass > 0) {
			return 16777215;
		}
		
		if(stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("display");
			
			if(tag.hasKey("color")) {
				return tag.getInteger("color");
			}
			else {
				List list = this.getEffects(stack);

	            if (list != null && !list.isEmpty())
	            {
	                return PotionCoreHelper.getCustomPotionColor(list);
	            }
			}
		}
			
        return super.getColorFromItemStack(stack, pass);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List tabList) {
		for(PotionData data : PotionCoreEffects.potionMap.values()) {
			if(data != null && data.potion != null) {
				data.potion.getCreativeItems(tabList);
			}
		}
	}
	
}
