package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.events.LevelUpEvent;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public class LevelData implements ILevelDataStorage {
	private int level = 0;
	private int freeSkillPoint = 0;
	private double exp = 0.0D;

	private boolean isLevelUp = false;
	private boolean isLevelDown = false;

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
		if (level < 0 || level > PonyMagic.MAX_LVL) {
			return;
		}
		this.setExp(PonyMagic.EXP_FOR_LVL.get(level));
	}

	@Override
	@Deprecated
	public boolean isLevelUp() {
		return this.level < PonyMagic.MAX_LVL && this.isLevelUp;
	}
	
	@Override
	@Deprecated
	public void levelUp(EntityPlayer player) {
		if (this.getLevel() == PonyMagic.MAX_LVL)
			return;
		this.setLevel(this.getLevel() + 1);
	}

	@Override
	public boolean isLevelChange() {
		if (this.level < PonyMagic.MAX_LVL && this.exp >= PonyMagic.EXP_FOR_LVL.get(this.level + 1)) {
			this.isLevelUp = true;
		} else if (this.exp < PonyMagic.EXP_FOR_LVL.get(this.level)) {
			this.isLevelDown = true;
		}

		return this.isLevelUp || this.isLevelDown;
	}

	@Override
	public void changeLevel() {
		if (!this.isLevelChange()) return;

		if (this.isLevelUp && this.level < PonyMagic.MAX_LVL) {
			// Make level up
			this.level += 1;
			if (this.level % 3 == 0) {
				// Add free skill point for every 3 level
				this.freeSkillPoint += 1;
			}
			this.isLevelUp = false;

		} else if (this.isLevelDown && this.level > 0) {
			// Make level down
			if (this.level % 3 == 0) {
				// Remove free skill point if level was ф multiple of three
				this.freeSkillPoint -= 1;
			}
			this.level -= 1;
			this.isLevelDown = false;
		}
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

	@Override
	public double getExp() {
		return this.exp;
	}

	@Override
	public void setExp(double exp) {
		if (Config.expModifier) {
			exp *= Config.expModifierAmount;
		}

		if (exp < 0 && this.level == 0) {
			exp = 0.0D;
		}
		if (exp > PonyMagic.EXP_FOR_LVL.get(PonyMagic.MAX_LVL)) {
			exp = PonyMagic.EXP_FOR_LVL.get(PonyMagic.MAX_LVL);
		}

		this.exp = exp;
	}

	@Override
	public void addExp(double exp) {
		this.setExp(this.exp + exp);
	}
}
