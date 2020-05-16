package net.braunly.ponymagic.skill;

import java.util.Map;

public class Skill {
    private final String name;
    private final int price;
    private final int skillLevel;
    private final int stamina;
    private final Map<String, Integer> effect;
    private final Map<String, Integer> spellData;
    private final Map<String, Integer> depends;

    public Skill(String name, int price, int skillLevel, int stamina, Map<String, Integer> effect, Map<String, Integer> spellData, Map<String, Integer> depends) {
        this.name = name;
        this.price = price;
        this.skillLevel = skillLevel;
        this.stamina = stamina;
        this.effect = effect;
        this.spellData = spellData;
        this.depends = depends;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return price;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public Map<String, Integer> getEffect() {
        return effect;
    }

    public Map<String, Integer> getSpellData() {
        return spellData;
    }

    public int getStamina() {
        return stamina;
    }

    public Map<String, Integer> getDepends() {
        return depends;
    }
}
