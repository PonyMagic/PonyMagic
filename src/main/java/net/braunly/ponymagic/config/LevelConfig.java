package net.braunly.ponymagic.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.quests.LevelGoal;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LevelConfig {
    // Race quests config
    private static final Map<String, ImmutableMap<Integer, LevelGoal>> raceConfigs = new HashMap<>();

    public static void load(File configDir) {
        String CONFIG_DIR = configDir.getAbsolutePath() + "/" + PonyMagic.MODID;
        PonyMagic.log.info(CONFIG_DIR);
        for (EnumRace race : EnumRace.values()) {
            // Load config only for playable races
            if (race.equals(EnumRace.REGULAR) || race.equals(EnumRace.ALICORN)) continue;

            PonyMagic.log.info("Loading level config for {}", race.name().toLowerCase());
            File raceLevelConfigFile = new File(CONFIG_DIR, race.name().toLowerCase() + ".json");
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
                    Map<String, ImmutableMap<String, Integer>> levelQuestsData = new HashMap<>();

                    // Load level requirements config
                    JsonObject levelQuestsConfig = (JsonObject) jsonRaceConfig.get(Integer.toString(level));
                    for (Map.Entry<String, JsonElement> questEntry : levelQuestsConfig.entrySet()) {
                        String questName = questEntry.getKey();
                        // Get quest goals
                        Map<String, Integer> questGoals = gson.fromJson(questEntry.getValue(), HashMap.class);

                        levelQuestsData.put(
                                questName,
                                ImmutableMap.copyOf(questGoals)
                        );
                    }
                    raceLevels.put(
                            level,
                            new LevelGoal(level, ImmutableMap.copyOf(levelQuestsData))
                    );
                }
            } else {
                PonyMagic.log.entry("Level config not loaded!");
            }
            raceConfigs.put(
                    race.name(),
                    ImmutableMap.copyOf(raceLevels)
            );
        }
    }

    public static LevelGoal getRaceLevelConfig(EnumRace race, int level) {
        return raceConfigs.get(race.name()).get(level);
    }

    private static void makeDefaultConfig(File configFile) {
        try {
            String DEFAULT_CONFIG_PATH = "/assets/" + PonyMagic.MODID + "/level_configs/";
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
