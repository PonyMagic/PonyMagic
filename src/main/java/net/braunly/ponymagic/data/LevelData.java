package net.braunly.ponymagic.data;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LevelData {
	public HashMap<String,Number> levelData = new HashMap<String,Number>();
	
	public void loadNBTData(NBTTagCompound compound) {
		HashMap<String,Number> levelData = new HashMap<String,Number>();
		if(compound == null)
			return;
		NBTTagCompound nbttagcompound = compound;
        levelData.put("Level", nbttagcompound.getInteger("Level"));
        levelData.put("FreeSkillPoint", nbttagcompound.getInteger("FreeSkillPoint"));
        levelData.put("Exp", nbttagcompound.getDouble("Exp"));
        this.levelData = levelData;
	}

	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("Level", (Integer) levelData.get("Level"));
		nbttagcompound.setInteger("FreeSkillPoint", (Integer) levelData.get("FreeSkillPoint"));
		nbttagcompound.setDouble("Exp", (Double) levelData.get("Exp"));
		compound.setTag("LevelData", nbttagcompound);
	}

	public int getLevel() {
		if(levelData.containsKey("Level")){
			return (Integer) levelData.get("Level");
		}
		return 0;
	}

	public void upLevel() {
		if(!levelData.containsKey("Level")){
			levelData.put("Level", 1);
		}
		levelData.put("Level", (Integer) levelData.get("Level") + 1);
	}
	
	public int getFreeSkillPoints() {
		if(levelData.containsKey("FreeSkillPoint")){
			return (Integer) levelData.get("FreeSkillPoint");
		}
		return 0;
	}
	
	public void upFreeSkillPoints() {
		if(!levelData.containsKey("FreeSkillPoint")){
			levelData.put("FreeSkillPoint", 1);
		}
		levelData.put("FreeSkillPoint", (Integer) levelData.get("FreeSkillPoint") + 1);
	}
	
	public double getExp() {
		if(levelData.containsKey("Exp")){
			return (Double) levelData.get("Exp");
		}
		return 0;
	}
	
	public void increaseExp(double exp) {
		if(!levelData.containsKey("Exp")){
			levelData.put("Exp", 0);
		}
		levelData.put("Exp", (Double) levelData.get("Exp") + exp);
	}

}
