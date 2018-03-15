package net.braunly.ponymagic.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.NoSuchElementException;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.race.EnumRace;
import net.braunly.ponymagic.util.NBTJsonUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class PlayerData implements ICapabilityProvider {

	@CapabilityInject(PlayerData.class)
	public static final Capability<PlayerData> PLAYERDATA_CAPABILITY = null;

	public LevelData levelData = new LevelData();
	public SkillData skillData = new SkillData();

	public NBTTagCompound cloned;

	public EntityPlayer player = null;

	public String playername = "";
	public String uuid = "";
	public EnumRace race = EnumRace.REGULAR;

	public void reset() {
		this.skillData.reset();
		this.levelData.addExp(-1 * (this.levelData.getExp() / 10)); // -10%
		this.levelData.setFreeSkillPoints(this.levelData.getLevel() / 3);
	}
	
	public void clean() {
		this.levelData = new LevelData();
		this.skillData = new SkillData();
	}

	public void setNBT(NBTTagCompound data) {
		this.levelData.loadNBTData(data);
		this.skillData.loadNBTData(data);

		if (this.player != null) {
			this.playername = this.player.getName();
			this.uuid = this.player.getPersistentID().toString();
		} else {
			this.playername = data.getString("PlayerName");
			this.uuid = data.getString("UUID");
		}

		try {
			this.race = EnumRace.getByName(data.getString("Race")).get();
		} catch (NoSuchElementException e) {
			this.race = EnumRace.REGULAR;
		}
	}

	public NBTTagCompound getNBT() {
		if (this.player != null) {
			this.playername = this.player.getName();
			this.uuid = this.player.getPersistentID().toString();
		}
		NBTTagCompound compound = new NBTTagCompound();
		this.levelData.saveNBTData(compound);
		this.skillData.saveNBTData(compound);

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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == PLAYERDATA_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (hasCapability(capability, facing))
			return (T) this;
		return null;
	}

	public void save() {
		final NBTTagCompound compound = getNBT();
		final String filename = uuid + ".json";

		try {
			File saveDir = PlayerDataController.getWorldSaveDirectory();
			File file = new File(saveDir, filename + "_new");
			File file1 = new File(saveDir, filename);
			NBTJsonUtil.SaveFile(file, compound);
			if (file1.exists()) {
				file1.delete();
			}
			file.renameTo(file1);
		} catch (Exception e) {
			PonyMagic.log.catching(e);
		}
	}

	public static NBTTagCompound loadPlayerDataOld(String player) {
		File saveDir = PlayerDataController.getWorldSaveDirectory();
		String filename = player;
		if (filename.isEmpty())
			filename = "noplayername";
		filename += ".dat";
		try {
			File file = new File(saveDir, filename);
			if (file.exists()) {
				NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(file));
				file.delete();
				file = new File(saveDir, filename + "_old");
				if (file.exists())
					file.delete();
				return comp;
			}
		} catch (Exception e) {
			PonyMagic.log.catching(e);
		}
		try {
			File file = new File(saveDir, filename + "_old");
			if (file.exists()) {
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			}

		} catch (Exception e) {
			PonyMagic.log.catching(e);
		}

		return new NBTTagCompound();
	}

	public static NBTTagCompound loadPlayerData(String player) {
		File saveDir = PlayerDataController.getWorldSaveDirectory();
		String filename = player;
		if (filename.isEmpty())
			filename = "noplayername";
		filename += ".json";
		File file = null;
		try {
			file = new File(saveDir, filename);
			if (file.exists()) {
				return NBTJsonUtil.LoadFile(file);
			}
		} catch (Exception e) {
			PonyMagic.log.error("Error loading: " + file.getAbsolutePath());
			PonyMagic.log.catching(e);
		}

		return new NBTTagCompound();
	}

	public static PlayerData get(EntityPlayer player) {
		PlayerData data = player.getCapability(PLAYERDATA_CAPABILITY, null);
		if (data.player == null) {
			data.player = player;

			NBTTagCompound compound = loadPlayerData(player.getPersistentID().toString());
			if (compound.hasNoTags()) {
				compound = loadPlayerDataOld(player.getName());
			}
			data.setNBT(compound);
		}
		return data;
	}
}
