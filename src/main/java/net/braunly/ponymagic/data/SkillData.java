package net.braunly.ponymagic.data;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SkillData {
	private HashMap<String, Integer> skillData = new HashMap<String, Integer>();

	public void loadNBTData(NBTTagCompound compound) {
		HashMap<String, Integer> skillData = new HashMap<String, Integer>();

		if (compound == null)
			return;

		NBTTagList list = compound.getTagList("Skills", 10);
		if (list == null) {
			return;
		}

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			skillData.put(nbttagcompound.getString("Name"), nbttagcompound.getInteger("Level"));
		}
		this.skillData = skillData;
	}

	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (String skillName : this.skillData.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", skillName);
			nbttagcompound.setInteger("Level", getSkillLevel(skillName));
			list.appendTag(nbttagcompound);
		}

		compound.setTag("Skills", list);
	}

	public int getSkillLevel(String name) {
		if (!this.skillData.containsKey(name)) {
			this.skillData.put(name, 0);
			return 0;
		}
		return this.skillData.get(name);
	}

	public boolean isSkillLearned(String skillName) {
		if (getSkillLevel(skillName) > 0) {
			return true;
		}
		return false;
	}

	public void upLevel(String name) {
		this.skillData.put(name, getSkillLevel(name) + 1);
	}

	public void reset() {
		this.skillData.clear();
	}
}
