package net.braunly.ponymagic.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortalConfig {
    private static String configFilePath = null;
    @Getter
    private static final Map<String, BlockPos> portalsMap = new HashMap<>();

    public static void init(File modConfigDir) {
        configFilePath = modConfigDir.getAbsolutePath() + "/" + PonyMagic.MODID + "/portals.json";
        load();
    }

    public static void load() {
        if (configFilePath == null) {
            PonyMagic.log.error("Portals config not initialized!");
            return;
        }
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            save();
            PonyMagic.log.info("Portals config created!");
            return;
        }

        PonyMagic.log.info("Loading portals config");
        Gson gson = new Gson();
        JsonObject jsonPortalsMap = null;

        try {
            // Load portals config file
            jsonPortalsMap = gson.fromJson(new FileReader(configFile), JsonObject.class);
        } catch (FileNotFoundException exc ) {
            // already checked above
            exc.printStackTrace();
        }
        Type typeToken = new TypeToken<HashMap<String, List<Integer>>>(){}.getType();
        HashMap<String, List<Integer>> clonedMap = gson.fromJson(jsonPortalsMap, typeToken);
        if (clonedMap != null) {
            for (Map.Entry<String, List<Integer>> entry : clonedMap.entrySet()) {
                List<Integer> cords = entry.getValue();
                portalsMap.put(entry.getKey(), new BlockPos(cords.get(0), cords.get(1), cords.get(2)));
            }
        } else {
            PonyMagic.log.info("Portals config format error!");
        }
    }

    public static void addPortal(String name, BlockPos cords) {
        portalsMap.put(name, cords);
        save();
    }

    public static BlockPos getPortal(String name) {
        return portalsMap.get(name);
    }

    public static void deletePortal(String name) {
        portalsMap.remove(name);
        save();
    }

    public static void save() {
        Gson gson = new Gson();
        Map<String, Integer[]> saveMap = new HashMap<>();
        for (Map.Entry<String, BlockPos> entry: portalsMap.entrySet()) {
            saveMap.put(
                    entry.getKey(),
                    new Integer[]{entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ()}
            );
        }

        try {
            FileWriter file = new FileWriter(configFilePath);
            gson.toJson(saveMap, file);
            file.close();
            PonyMagic.log.info("Portals config saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
