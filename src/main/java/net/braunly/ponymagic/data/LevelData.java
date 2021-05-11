package net.braunly.ponymagic.data;

import com.google.common.collect.ImmutableMap;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Map;

public class LevelData implements ILevelDataStorage {
	private int level = 0;
	private int freeSkillPoint = 0;

	private HashMap<String, HashMap<String, Integer>> currentGoals = new HashMap<>();

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound == null)
			return;
		HashMap<String, HashMap<String, Integer>> quests = new HashMap<>();

		NBTTagCompound levelTags = compound.getCompoundTag("LevelData");
		this.level = levelTags.getInteger("Level");
		this.freeSkillPoint = levelTags.getInteger("FreeSkillPoint");

		NBTTagList questsList = levelTags.getTagList("CurrentQuests", 10);

		for (int i = 0; i < questsList.tagCount(); i++) {
			NBTTagCompound questsCompound = questsList.getCompoundTagAt(i);
			HashMap<String, Integer> goalsMap = new HashMap<>();

			NBTTagList goalsList = questsCompound.getTagList("Goals", 10);
			for (int k = 0; k < goalsList.tagCount(); k++) {
				NBTTagCompound goalCompound = goalsList.getCompoundTagAt(k);
				goalsMap.put(
						goalCompound.getString("Goal"),
						goalCompound.getInteger("Count")
				);
			}
			quests.put(questsCompound.getString("QuestName"), goalsMap);
		}

		this.currentGoals = quests;
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("Level", this.getLevel());
		nbttagcompound.setInteger("FreeSkillPoint", this.getFreeSkillPoints());

		NBTTagList questsList = new NBTTagList();
		for (Map.Entry<String, HashMap<String, Integer>> questEntry : this.currentGoals.entrySet()) {
			NBTTagCompound questsCompound = new NBTTagCompound();
			questsCompound.setString("QuestName", questEntry.getKey());
			NBTTagList goalsList = new NBTTagList();
			for (Map.Entry<String, Integer> goalEntry : questEntry.getValue().entrySet()) {
				NBTTagCompound goalCompound = new NBTTagCompound();
				goalCompound.setString("Goal", goalEntry.getKey());
				goalCompound.setInteger("Count", goalEntry.getValue());
				goalsList.appendTag(goalCompound);
			}
			questsCompound.setTag("Goals", goalsList);
			questsList.appendTag(questsCompound);
		}
		nbttagcompound.setTag("CurrentQuests", questsList);

		compound.setTag("LevelData", nbttagcompound);
	}

	@Override
	public HashMap<String, HashMap<String, Integer>> getCurrentGoals() {
		return this.currentGoals;
	}

	@Override
	public boolean isCurrentGoal(String questName, String goalName) {
		if (this.currentGoals.isEmpty() || !this.currentGoals.containsKey(questName))
			return false;

		HashMap<String, Integer> questGoals = this.currentGoals.get(questName);

		// NOTE: Fix log (and etc.) variants (maybe?) (fixed? see 1fc29b7d)
		return !questGoals.isEmpty() && questGoals.containsKey(goalName);
	}

	@Override
	public void decreaseGoal(String questName, String goalName) {
		if (!isCurrentGoal(questName, goalName)) return;

		HashMap<String, Integer> questGoals = this.currentGoals.get(questName);

		int goal = questGoals.get(goalName);

		goal -= 1;

		if (goal == 0) {
			questGoals.remove(goalName);
		} else {
			questGoals.put(goalName, goal);
		}
		if (questGoals.isEmpty()) {
			this.currentGoals.remove(questName);
		} else {
			this.currentGoals.put(questName, questGoals);
		}
	}

	@Override
	public void setGoals(ImmutableMap<String, ImmutableMap<String, Integer>> goals) {
		this.currentGoals.clear();
		for (String questName : goals.keySet()) {
			this.currentGoals.put(
					questName,
					new HashMap<>(goals.get(questName))
			);
		}
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public void setLevel(int level) {
		if (level < 0 || level > PonyMagic.MAX_LVL) {
			return;
		}
		// Make level up
		this.level = level;
	}

	@Override
	public boolean isLevelUp() {
		// Level up when current goals is empty
		return this.level < PonyMagic.MAX_LVL && this.currentGoals.isEmpty();
	}
	
	@Override
	public void levelUp() {
		this.setLevel(this.level + 1);
		this.freeSkillPoint += 1;
	}

	@Override
	public int getFreeSkillPoints() {
		return this.freeSkillPoint;
	}

	@Override
	public void addFreeSkillPoints(int points) {
		this.freeSkillPoint += points;
	}

	@Override
	public void setFreeSkillPoints(int points) {
		this.freeSkillPoint = points;
	}
}
