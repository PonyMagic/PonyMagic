package net.braunly.ponymagic.util;

import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class QuestGoalUtils {
    private static final String delimiter = "#";

    public static String getConfigKey(EnumQuestGoalType goalType, ResourceLocation resLoc, int meta) {
        // TODO: Add .replace("lit_", "") to fix redstone_ore mining error?
        if (meta > 0) {
            return goalType.name() + delimiter + resLoc.toString() + delimiter + meta;
        } else {
            return goalType.name() + delimiter + resLoc.toString();
        }
    }

    public static EnumQuestGoalType getGoalType(String configKey) {
        return EnumQuestGoalType.getByName(configKey.split(delimiter)[0]).orElse(EnumQuestGoalType.CUSTOM);
    }

    public static ResourceLocation getResLoc(String configKey) {
        return new ResourceLocation(configKey.split(delimiter)[1]);
    }

    public static int getMeta(String configKey) {
        return configKey.split(delimiter).length > 2 ? Integer.parseInt(configKey.split(delimiter)[2]) : 0;
    }

    public static ItemStack getItemStack(String configKey) {
        EnumQuestGoalType type = getGoalType(configKey);
        Item item = null;
        if (type == EnumQuestGoalType.BLOCK) {
            item = Item.getItemFromBlock(Block.REGISTRY.getObject(getResLoc(configKey)));
        } else if (type == EnumQuestGoalType.ITEM) {
            item = Item.REGISTRY.getObject(getResLoc(configKey));
        }
        if (item == null) {
            item = Items.AIR;
        }
        return new ItemStack(item, 1, getMeta(configKey));
    }
    
    public static String getLocalizedGoalName(String configKey) {
        EnumQuestGoalType type = getGoalType(configKey);
        switch (type) {
            case BLOCK:
            case ITEM:
                ItemStack item = getItemStack(configKey);
                return item.getDisplayName();
            case ENTITY:
                String name = EntityList.getTranslationName(getResLoc(configKey));
                if (name != null) {
                    return name;
                }
                PonyMagic.log.warn("Can't get localized goal name");
            case CUSTOM:
                return "custom";
            default:
                return null;
        }
    }
}
