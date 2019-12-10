package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.ILevelDataStorage;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import me.braunly.ponymagic.api.interfaces.ISkillDataStorage;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.NoSuchElementException;

public class PlayerData implements IPlayerDataStorage {
	private ILevelDataStorage levelData = new LevelData();
	private ISkillDataStorage skillData = new SkillData();

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
	public EnumRace getRace() {
		return this.race;
	}

	@Override
	public void setRace(EnumRace race) {
		this.race = race;
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

	@Override
	public void reset() {
		this.skillData.reset();
		this.levelData.addExp(-1 * (this.levelData.getExp() / 10)); // -10%
		this.levelData.setFreeSkillPoints(this.levelData.getLevel() / 3);
		addDefaultSpell();
	}

	@Override
	public void clean() {
		this.levelData = new LevelData();
		this.skillData = new SkillData();
		addDefaultSpell();
	}

	@Override
	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		this.levelData.saveToNBT(compound);
		this.skillData.saveToNBT(compound);

		compound.setString("PlayerName", this.playername);
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
		this.levelData.readFromNBT(nbt);
		this.skillData.readFromNBT(nbt);

		try {
			this.race = EnumRace.getByName(nbt.getString("Race")).get();
		} catch (NoSuchElementException e) {
			this.race = EnumRace.REGULAR;
		}
	}

	private void addDefaultSpell() {
		// Add ONLY ONE default spell
		this.skillData.upSkillLevel(this.race.getDefaultSpell());
	}
}
