package net.braunly.ponymagic.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.skill.Skill;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SkillConfig {
    private static String skillsConfigDir = null;
    private static final Map<EnumRace, ImmutableMap<String, Skill>> raceSkillsConfig = new HashMap<>();

    private SkillConfig() {
        throw new IllegalStateException("Utility class");
    }


    public static void init(File modConfigDir) {
        skillsConfigDir = modConfigDir.getAbsolutePath() + "/" + PonyMagic.MODID + "/skills";
        load();
    }

    public static void load() {
        if (skillsConfigDir == null) {
            PonyMagic.log.error("Skills config not initialized!");
            return;
        }
        for (EnumRace race : EnumRace.values()) {
            // Load config only for playable races
            if (race.equals(EnumRace.ALICORN)) continue;

            PonyMagic.log.info("Loading skills config for {}", race.name().toLowerCase());
            File raceSkillsConfigFile = new File(
                    skillsConfigDir,
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
            } catch (FileNotFoundException exception) {
                // already checked above
                PonyMagic.log.catching(exception);
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
                PonyMagic.log.error("Skills config not loaded!");
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
            String defaultConfigPath = "/assets/" + PonyMagic.MODID + "/config/skills/";
            InputStream inputStream = LevelConfig.class.getResourceAsStream(
                    defaultConfigPath + configFile.getName()
            );
            FileUtils.copyInputStreamToFile(inputStream, configFile);
            PonyMagic.log.info("Created default skills config {}", configFile.getName());
        } catch (IOException exception) {
            PonyMagic.log.catching(exception);
        }
    }
}
