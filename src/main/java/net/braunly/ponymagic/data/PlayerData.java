package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.ISkillDataStorage;
import me.braunly.ponymagic.api.interfaces.ITickDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.NoSuchElementException;

public class PlayerData implements IPlayerDataStorage {
	private ILevelDataStorage levelData = new LevelData();
	private ISkillDataStorage skillData = new SkillData();
	private ITickDataStorage tickData = new TickData();

	public String version = "v2.5.4";
	public EntityPlayer player = null;

	private String playername = null;
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
		if (this.race == EnumRace.REGULAR || race == EnumRace.REGULAR) {
			this.levelData = new LevelData();
		} else {
			// Remove % of exp
			this.levelData.addExp(-1 * (this.levelData.getExp() / Config.raceExpPercentForRaceChange));
			// Restore skill points for current level. Not duplicated with LevelData.changeLevel()!
			this.levelData.setFreeSkillPoints(this.levelData.getLevel() / 3);
		}
		this.race = race;
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
        this.playername = player.getName();
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
		this.levelData.addExp(-1 * (this.levelData.getExp() / Config.raceExpPercentForSkillReset));
		// Restore skill points for current level. Not duplicated with LevelData.changeLevel()!
		this.levelData.setFreeSkillPoints(this.levelData.getLevel() / 3);
		this.tickData.reset();
		this.addDefaultSpell();
	}

	@Override
	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		this.levelData.saveToNBT(compound);
		this.skillData.saveToNBT(compound);
		this.tickData.saveToNBT(compound);

		compound.setString("PlayerName", this.playername);
		compound.setString("UUID", this.uuid);
		if (this.race != null) {
			compound.setString("Race", this.race.name());
		} else {
			compound.setString("Race", EnumRace.REGULAR.name());
		}

		compound.setString("Version", this.version);

		return compound;
	}

	@Override
	public void setNBT(NBTTagCompound nbt) {
		this.levelData.readFromNBT(nbt);
		this.skillData.readFromNBT(nbt);
		this.tickData.readFromNBT(nbt);

		try {
			this.race = EnumRace.getByName(nbt.getString("Race")).get();
		} catch (NoSuchElementException e) {
			this.race = EnumRace.REGULAR;
		}
		if (!this.version.equals(nbt.getString("Version"))) {
			this.migrateTo(this.version);
		}

	}

	private void migrateTo(String version) {
		if (version.equals("v2.5.4")) {
			this.levelData.setExp(PonyMagic.EXP_FOR_LVL.get(this.levelData.getLevel()) + this.levelData.getExp());
			if (this.race == EnumRace.UNICORN) {
				this.skillData.reset();
			}
		}
	}

	private void addDefaultSpell() {
		// Add ONLY ONE default spell
		this.skillData.upSkillLevel(this.race.getDefaultSpell());
	}
}
