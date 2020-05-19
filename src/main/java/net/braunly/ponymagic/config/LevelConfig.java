package net.braunly.ponymagic.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.quests.LevelGoal;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LevelConfig {
    private static String levelingConfigDir = null;
    // Race quests config
    private static final Map<EnumRace, ImmutableMap<Integer, LevelGoal>> raceConfigs = new HashMap<>();

    public static void init(File modConfigDir) {
        levelingConfigDir = modConfigDir.getAbsolutePath() + "/" + PonyMagic.MODID + "/leveling";
        load();
    }

    public static void load() {
        if (levelingConfigDir == null) {
            PonyMagic.log.error("Leveling config not initialized!");
            return;
        }
        for (EnumRace race : EnumRace.values()) {
            // Load config only for playable races
            if (race.equals(EnumRace.REGULAR) || race.equals(EnumRace.ALICORN)) continue;

            PonyMagic.log.info("Loading level config for {}", race.name().toLowerCase());
            File raceLevelConfigFile = new File(
                    levelingConfigDir,
                    race.name().toLowerCase() + ".json"
            );
            if (!raceLevelConfigFile.exists()) {
                // Copy default configs
                makeDefaultConfig(raceLevelConfigFile);
            }
            Gson gson = new Gson();
            JsonObject jsonRaceConfig = null;
            // Set of new levels requirements
            Map<Integer, LevelGoal> raceLevels = new HashMap<>();
            try {
                // Load race config file
                jsonRaceConfig = gson.fromJson(new FileReader(raceLevelConfigFile), JsonObject.class);
            } catch (FileNotFoundException exc ) {
                // already checked above
                exc.printStackTrace();
            }
            if (jsonRaceConfig != null) {
                for (int level = 1; level <= PonyMagic.MAX_LVL; level++) {
                    // Set of quests goals for level
                    ImmutableMap.Builder<String, ImmutableMap<String, Integer>> levelQuestsDataBuilder = new ImmutableMap.
                            Builder<>();

                    // Load level requirements config
                    JsonObject levelQuestsConfig = (JsonObject) jsonRaceConfig.get(Integer.toString(level));
                    for (Map.Entry<String, JsonElement> questEntry : levelQuestsConfig.entrySet()) {
                        String questName = questEntry.getKey();
                        // Get quest goals
                        HashMap<String, Integer> questGoals = gson.fromJson(questEntry.getValue(), new TypeToken<HashMap<String, Integer>>(){}.getType());

                        levelQuestsDataBuilder.put(
                                questName,
                                ImmutableMap.copyOf(questGoals)
                        );
                    }
                    raceLevels.put(
                            level,
                            new LevelGoal(level, levelQuestsDataBuilder.build())
                    );
                }
            } else {
                PonyMagic.log.entry("Level config not loaded!");
            }
            raceConfigs.put(
                    race,
                    ImmutableMap.copyOf(raceLevels)
            );
        }
    }

    public static LevelGoal getRaceLevelConfig(EnumRace race, int level) {
        return raceConfigs.get(race).get(level);
    }

    private static void makeDefaultConfig(File configFile) {
        try {
            String DEFAULT_CONFIG_PATH = "/assets/" + PonyMagic.MODID + "/config/leveling/";
            InputStream inputStream = LevelConfig.class.getResourceAsStream(
                    DEFAULT_CONFIG_PATH + configFile.getName()
            );
            FileUtils.copyInputStreamToFile(inputStream, configFile);
            PonyMagic.log.info("Created default level config {}", configFile.getName());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
