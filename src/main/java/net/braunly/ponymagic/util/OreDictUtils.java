package net.braunly.ponymagic.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtils {
    private final ImmutableList<Integer> oreDictIds;
    protected OreDictUtils() {
        ImmutableList.Builder<Integer> oreDictIdsBuilder = new ImmutableList.Builder<>();

        for (String oreDictName : OreDictionary.getOreNames()) {
            if (oreDictName.startsWith("ore")) {
                oreDictIdsBuilder.add(OreDictionary.getOreID(oreDictName));
            }
        }

        oreDictIds = oreDictIdsBuilder.build();
    }

    private static class OreDictUtilsHolder {
        static final OreDictUtils INSTANCE = new OreDictUtils();
    }

    public static OreDictUtils getInstance() {
        return OreDictUtils.OreDictUtilsHolder.INSTANCE;
    }

    public boolean isOre(ItemStack itemStack) {
        if (itemStack.isEmpty()) return false;

        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            if (oreDictIds.contains(oreId)) {
                return true;
            }
        }
        return false;
    }
}
