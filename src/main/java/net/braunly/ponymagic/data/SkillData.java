package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.ISkillDataStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SkillData implements ISkillDataStorage {
	private HashMap<String, Integer> skillDataMap = new HashMap<>();

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		HashMap<String, Integer> skillDataNew = new HashMap<>();

		if (compound == null)
			return;

		NBTTagList list = compound.getTagList("Skills", 10);
		if (list.hasNoTags()) {
			return;
		}

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			skillDataNew.put(nbttagcompound.getString("Name"), nbttagcompound.getInteger("Level"));
		}
		this.skillDataMap = skillDataNew;
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (String skillName : this.skillDataMap.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", skillName);
			nbttagcompound.setInteger("Level", this.getSkillLevel(skillName));
			list.appendTag(nbttagcompound);
		}

		compound.setTag("Skills", list);
	}

	@Override
	public int getSkillLevel(String skillName) {
		if (!this.skillDataMap.containsKey(skillName)) {
			return 0;
		}
		return this.skillDataMap.get(skillName);
	}

	@Override
	public boolean isAnySkillLearned(Map<String, Integer> skillsMap) {
		if (skillsMap == null || skillsMap.isEmpty())
			return true;
		return !Collections.disjoint(this.skillDataMap.entrySet(), skillsMap.entrySet());
	}

	@Override
	public boolean isSkillLearned(String skillName) {
		return this.getSkillLevel(skillName) > 0;
	}

	@Override
	public void upSkillLevel(String skillName) {
		this.skillDataMap.put(skillName, this.getSkillLevel(skillName) + 1);
	}

	@Override
	public void reset() {
		this.skillDataMap.clear();
	}
}
