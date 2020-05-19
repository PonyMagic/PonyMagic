package net.braunly.ponymagic.quests;

import com.google.common.collect.ImmutableMap;

import java.util.Set;

public class LevelGoal {
    private final int level;
    private final ImmutableMap<String, ImmutableMap<String, Integer>> quests;

    public LevelGoal(int level, ImmutableMap<String, ImmutableMap<String, Integer>> quests) {
        this.level = level;
        this.quests = quests;
    }

    public int getLevel() {
        return this.level;
    }

    public ImmutableMap<String, ImmutableMap<String, Integer>> getQuestsWithGoals() {
        return this.quests;
    }

    public Set<String> getQuests() {
        return this.quests.keySet();
    }

    public ImmutableMap<String, Integer> getGoalsForQuest(String quest) {
        return this.quests.get(quest);
    }
}
