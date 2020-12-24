package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.ISkillDataStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SkillData implements ISkillDataStorage {
	private HashMap<String, Integer> skillData = new HashMap<String, Integer>();

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		HashMap<String, Integer> skillData = new HashMap<String, Integer>();

		if (compound == null)
			return;

		NBTTagList list = compound.getTagList("Skills", 10);
		if (list.hasNoTags()) {
			return;
		}

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			skillData.put(nbttagcompound.getString("Name"), nbttagcompound.getInteger("Level"));
		}
		this.skillData = skillData;
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (String skillName : this.skillData.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", skillName);
			nbttagcompound.setInteger("Level", this.getSkillLevel(skillName));
			list.appendTag(nbttagcompound);
		}

		compound.setTag("Skills", list);
	}

	@Override
	public int getSkillLevel(String skillName) {
		if (!this.skillData.containsKey(skillName)) {
			this.skillData.put(skillName, 0);
			return 0;
		}
		return this.skillData.get(skillName);
	}

	@Override
	public boolean isAnySkillLearned(Map<String, Integer> skillsMap) {
		if (skillsMap == null || skillsMap.isEmpty())
			return true;
		return !Collections.disjoint(this.skillData.entrySet(), skillsMap.entrySet());
	}

	@Override
	public boolean isSkillLearned(String skillName) {
		return this.getSkillLevel(skillName) > 0;
	}

	@Override
	public void upSkillLevel(String skillName) {
		this.skillData.put(skillName, this.getSkillLevel(skillName) + 1);
	}

	@Override
	public void reset() {
		this.skillData.clear();
	}
}
