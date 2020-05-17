package net.braunly.ponymagic.quests;

import java.util.HashMap;
import java.util.Set;

public class LevelGoal {
    private final int level;
    private final HashMap<String, HashMap<String, Integer>> quests;

    public LevelGoal(int level, HashMap<String, HashMap<String, Integer>> quests) {
        this.level = level;
        this.quests = quests;
    }

    public int getLevel() {
        return this.level;
    }

    public HashMap<String, HashMap<String, Integer>> getQuestsWithGoals() {
        return this.quests;
    }

    public Set<String> getQuests() {
        return this.quests.keySet();
    }

    public HashMap<String, Integer> getGoalsForQuest(String quest) {
        return this.quests.get(quest);
    }
}
