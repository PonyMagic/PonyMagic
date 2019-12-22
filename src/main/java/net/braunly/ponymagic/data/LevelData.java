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
		this.level = level;
	}

	@Override
	public boolean isLevelUp() {
		return this.getLevel() < PonyMagic.MAX_LVL && this.getExp() >= (this.getLevel() + 1) * Config.expPerLevel;
	}
	@Override
	public void levelUp(EntityPlayer player) {
		if (this.getLevel() == PonyMagic.MAX_LVL)
			return;
		this.setLevel(this.getLevel() + 1);
		this.addExp(Config.expPerLevel * this.getLevel() * -1);
		if (this.getLevel() % 3 == 0) {
			this.addFreeSkillPoints(1);
		}
		MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, this.getLevel()));
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
		this.exp = exp;
	}

	@Override
	public void addExp(double exp) {
		if (Config.expModifier) {
			exp *= Config.expModifierAmount;
		}
		this.exp = getExp() + exp;
		if (this.exp < 0 && this.level == 0) {
			this.exp = 0.0D;
		}
	}
}
