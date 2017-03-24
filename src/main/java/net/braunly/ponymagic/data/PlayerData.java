package net.braunly.ponymagic.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;


public class PlayerData implements IExtendedEntityProperties{
	
	public LevelData levelData = new LevelData();
	
	public NBTTagCompound cloned;
	
	public EntityPlayer player;

	public String playername = "";
	public String uuid = "";


	@Override
	public void saveNBTData(NBTTagCompound compound) {
		PlayerDataController.instance.savePlayerData(this);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound data = PlayerDataController.instance.loadPlayerData(player.getPersistentID().toString());
		if(data.hasNoTags()){
			data = PlayerDataController.instance.loadPlayerDataOld(player.getCommandSenderName());
		}
		setNBT(data);
	}

	public void setNBT(NBTTagCompound data){
		levelData.loadNBTData(data);
		
		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		} else {
			playername = data.getString("PlayerName");
			uuid = data.getString("UUID");
		}
	}
	
	public NBTTagCompound getNBT() {
		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		}
		NBTTagCompound compound = new NBTTagCompound();
		levelData.saveNBTData(compound);
		
		compound.setString("PlayerName", playername);
		compound.setString("UUID", uuid);
		
		return compound;
	}

	@Override
	public void init(Entity entity, World world) {
		
	}

}