package net.braunly.ponymagic.data;

import lombok.Getter;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.ISkillDataStorage;
import me.braunly.ponymagic.api.interfaces.ITickDataStorage;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.config.LevelConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.NoSuchElementException;

public class PlayerData implements IPlayerDataStorage {
	private ILevelDataStorage levelData = new LevelData();
	private ISkillDataStorage skillData = new SkillData();
	private ITickDataStorage tickData = new TickData();

	@Getter
	private EntityPlayer player = null;

	private String playerName = null;
	private String uuid = null;
	private EnumRace race = EnumRace.REGULAR;

	@Override
	public ILevelDataStorage getLevelData() {
		return this.levelData;
	}

	@Override
	public ISkillDataStorage getSkillData() {
		return this.skillData;
	}

	@Override
	public ITickDataStorage getTickData() {
		return this.tickData;
	}

	@Override
	public EnumRace getRace() {
		return this.race;
	}

	@Override
	public void setRace(EnumRace race) {
		if (this.race != EnumRace.REGULAR && race != EnumRace.REGULAR) {
			int newLevel = this.levelData.getLevel() - Config.getRemoveLevelsForRaceChange();
			if (newLevel < 0) newLevel = 0;

			this.levelData.setLevel(newLevel);
			this.levelData.setFreeSkillPoints(newLevel);
		} else {
			this.levelData = new LevelData();
		}
		this.race = race;
		this.levelData.setGoals(LevelConfig.getRaceLevelConfig(
				race,
				this.levelData.getLevel() + 1
		).getQuestsWithGoals());
		this.skillData = new SkillData();
		this.tickData = new TickData();
		this.addDefaultSpell();
	}

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
        this.playerName = player.getName();
        this.uuid = player.getPersistentID().toString();
    }

	@Override
	public String getUUID() {
		return this.uuid;
	}

	/**
	 * Reset skills tree
	 */
	@Override
	public void reset() {
		this.skillData.reset();
		this.levelData.setFreeSkillPoints(this.levelData.getLevel());
		this.tickData.reset();
		this.addDefaultSpell();
	}

	@Override
	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		this.levelData.saveToNBT(compound);
		this.skillData.saveToNBT(compound);
		this.tickData.saveToNBT(compound);

		compound.setString("PlayerName", this.playerName);
		compound.setString("UUID", this.uuid);
		if (this.race != null) {
			compound.setString("Race", this.race.name());
		} else {
			compound.setString("Race", EnumRace.REGULAR.name());
		}
		return compound;
	}

	@Override
	public void setNBT(NBTTagCompound nbt) {
		try {
			this.race = EnumRace.getByName(nbt.getString("Race")).get();
			this.levelData.readFromNBT(nbt);
			this.skillData.readFromNBT(nbt);
			this.tickData.readFromNBT(nbt);
		} catch (NoSuchElementException e) {
			this.setRace(EnumRace.REGULAR);
		}

	}

	private void addDefaultSpell() {
		// Add ONLY ONE default spell
		this.skillData.upSkillLevel(this.race.getDefaultSpell());
	}
}
