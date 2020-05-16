package net.braunly.ponymagic.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.skill.Skill;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SkillConfig {
    private static final Map<EnumRace, ImmutableMap<String, Skill>> raceSkillsConfig = new HashMap<>();

    public static void load(File configDir) {
        String CONFIG_DIR = configDir.getAbsolutePath() + "/" + PonyMagic.MODID + "/skills";
        for (EnumRace race : EnumRace.values()) {
            // Load config only for playable races
            if (race.equals(EnumRace.REGULAR) || race.equals(EnumRace.ALICORN)) continue;

            PonyMagic.log.info("Loading skills config for {}", race.name().toLowerCase());
            File raceSkillsConfigFile = new File(
                    CONFIG_DIR,
                    race.name().toLowerCase() + ".json"
            );
            if (!raceSkillsConfigFile.exists()) {
                // Copy default configs
                makeDefaultConfig(raceSkillsConfigFile);
            }
            Gson gson = new Gson();
            Map<String, Skill> skills = new HashMap<>();
            JsonArray jsonSkillsArray = null;

            try {
                // Load race config file
                jsonSkillsArray = gson.fromJson(new FileReader(raceSkillsConfigFile), JsonArray.class);
            } catch (FileNotFoundException exc ) {
                // already checked above
                exc.printStackTrace();
            }
            if (jsonSkillsArray != null) {
                for (JsonElement jsonSkill: jsonSkillsArray) {
                    Skill skill = gson.fromJson(jsonSkill, Skill.class);
                    String skillName = skill.getName() + "#" + skill.getSkillLevel();
                    skills.put(
                            skillName,
                            skill
                    );
                }
            } else {
                PonyMagic.log.entry("Skills config not loaded!");
            }

            raceSkillsConfig.put(
                    race,
                    ImmutableMap.copyOf(skills)
            );
        }
    }

    public static Skill getRaceSkill(EnumRace race, String skillName, Integer skillLevel) {
        return raceSkillsConfig.get(race).get(skillName + "#" + skillLevel);
    }

    private static void makeDefaultConfig(File configFile) {
        try {
            String DEFAULT_CONFIG_PATH = "/assets/" + PonyMagic.MODID + "/config/skills/";
            InputStream inputStream = LevelConfig.class.getResourceAsStream(
                    DEFAULT_CONFIG_PATH + configFile.getName()
            );
            FileUtils.copyInputStreamToFile(inputStream, configFile);
            PonyMagic.log.info("Created default skills config {}", configFile.getName());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
