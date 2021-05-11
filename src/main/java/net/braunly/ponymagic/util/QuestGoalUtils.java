package net.braunly.ponymagic.util;

import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class QuestGoalUtils {
    private static final String DELIMITER = "#";

    private QuestGoalUtils
            () {
        throw new IllegalStateException("Utility class");
    }

    public static String getConfigKey(EnumQuestGoalType goalType, ResourceLocation resLoc, int meta) {
        // NOTE: Add .replace("lit_", "") to fix redstone_ore mining error?
        if (meta > 0) {
            return goalType.name() + DELIMITER + resLoc.toString() + DELIMITER + meta;
        } else {
            return goalType.name() + DELIMITER + resLoc.toString();
        }
    }

    public static EnumQuestGoalType getGoalType(String configKey) {
        return EnumQuestGoalType.getByName(configKey.split(DELIMITER)[0]).orElse(EnumQuestGoalType.ITEM);
    }

    public static ResourceLocation getResLoc(String configKey) {
        return new ResourceLocation(configKey.split(DELIMITER)[1]);
    }

    public static int getMeta(String configKey) {
        return configKey.split(DELIMITER).length > 2 ? Integer.parseInt(configKey.split(DELIMITER)[2]) : 0;
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

    @Nullable
    public static String getLocalizedGoalName(String configKey) {
        EnumQuestGoalType type = getGoalType(configKey);
        switch (type) {
            case BLOCK:
            case ITEM:
                ItemStack item = getItemStack(configKey);
                return item.getDisplayName();
            case ENTITY:
                return EntityList.getTranslationName(getResLoc(configKey));
            default:
                return null;
        }
    }
}
