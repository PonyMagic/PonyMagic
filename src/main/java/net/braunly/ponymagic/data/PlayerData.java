package net.braunly.ponymagic.data;

import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;


public class PlayerData implements IExtendedEntityProperties{
	
	public LevelData levelData = new LevelData();
	public SkillData skillData = new SkillData();
	
	public NBTTagCompound cloned;
	
	public EntityPlayer player;

	public String playername = "";
	public String uuid = "";
	public EnumRace race = EnumRace.REGULAR;


	@Override
	public void saveNBTData(NBTTagCompound compound) {
		PlayerDataController.instance.savePlayerData(this);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound data = PlayerDataController.instance.loadPlayerData(this.player.getPersistentID().toString());
		setNBT(data);
	}

	public void setNBT(NBTTagCompound data){
		this.levelData.loadNBTData(data);
		this.skillData.loadNBTData(data);
		
		if(this.player != null){
			this.playername = this.player.getDisplayName();
			this.uuid = this.player.getPersistentID().toString();
		} else {
			this.playername = data.getString("PlayerName");
			this.uuid = data.getString("UUID");
		}
		
		this.race = EnumRace.getByName(data.getString("Race"));
	}
	
	public NBTTagCompound getNBT() {
		if(this.player != null){
			this.playername = this.player.getCommandSenderName();
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
	public void init(Entity entity, World world) {
		
	}

}
