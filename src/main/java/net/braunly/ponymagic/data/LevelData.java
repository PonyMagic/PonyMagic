package net.braunly.ponymagic.data;

import net.minecraft.nbt.NBTTagCompound;

public class LevelData {
	private int level = 0;
	private int freeSkillPoint = 0;
	private double exp = 0.0D;

	public void loadNBTData(NBTTagCompound compound) {
		if (compound == null)
			return;
		NBTTagCompound levelTags = compound.getCompoundTag("LevelData");
		this.level = levelTags.getInteger("Level");
		this.freeSkillPoint = levelTags.getInteger("FreeSkillPoint");
		this.exp = levelTags.getDouble("Exp");
	}

	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("Level", this.getLevel());
		nbttagcompound.setInteger("FreeSkillPoint", this.getFreeSkillPoints());
		nbttagcompound.setDouble("Exp", this.getExp());
		compound.setTag("LevelData", nbttagcompound);
	}

	public int getLevel() {
		return this.level;
	}

	public void addLevel() {
		this.level += 1;
	}

	public void addLevel(int lvl) {
		this.level = getLevel() + lvl;
	}

	public int getFreeSkillPoints() {
		return this.freeSkillPoint;
	}

	public void addFreeSkillPoints() {
		this.freeSkillPoint += 1;
	}

	public void addFreeSkillPoints(int points) {
		this.freeSkillPoint = getFreeSkillPoints() + points;
	}

	public void setFreeSkillPoints(int points) {
		this.freeSkillPoint = points;
	}

	public double getExp() {
		return this.exp;
	}

	public void addExp(double exp) {
		this.exp = getExp() + exp;
		if (this.exp < 0) {
			resetExp();
		}
	}

	public void resetExp() {
		this.exp = 0.0D;
	}

}
