package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import net.minecraft.nbt.NBTTagCompound;

public class LevelData implements ILevelDataStorage {
	private int level = 0;
	private int freeSkillPoint = 0;
	private double exp = 0.0D;

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound == null)
			return;
		NBTTagCompound levelTags = compound.getCompoundTag("LevelData");
		this.level = levelTags.getInteger("Level");
		this.freeSkillPoint = levelTags.getInteger("FreeSkillPoint");
		this.exp = levelTags.getDouble("Exp");
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("Level", this.getLevel());
		nbttagcompound.setInteger("FreeSkillPoint", this.getFreeSkillPoints());
		nbttagcompound.setDouble("Exp", this.getExp());
		compound.setTag("LevelData", nbttagcompound);
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void addLevel() {
		this.level += 1;
	}

	@Override
	public int getFreeSkillPoints() {
		return this.freeSkillPoint;
	}

	@Override
	public void addFreeSkillPoints() {
		this.freeSkillPoint += 1;
	}

	@Override
	public void addFreeSkillPoints(int points) {
		this.freeSkillPoint += points;
	}

	@Override
	public void setFreeSkillPoints(int points) {
		this.freeSkillPoint = points;
	}

	@Override
	public double getExp() {
		return this.exp;
	}

	@Override
	public void addExp(double exp) {
		this.exp = getExp() + exp;
		if (this.exp < 0 && this.level == 0) {
			resetExp();
		}
	}

	@Override
	public void resetExp() {
		this.exp = 0.0D;
	}

}
