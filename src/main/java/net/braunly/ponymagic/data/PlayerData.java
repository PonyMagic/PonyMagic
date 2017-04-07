package net.braunly.ponymagic.data;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;


public class PlayerData implements IExtendedEntityProperties{
	
	public LevelData levelData = new LevelData();
	public SpellData spellData = new SpellData();
	
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
		NBTTagCompound data = PlayerDataController.instance.loadPlayerData(player.getPersistentID().toString());
		setNBT(data);
	}

	public void setNBT(NBTTagCompound data){
		levelData.loadNBTData(data);
		spellData.loadNBTData(data);
		
		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		} else {
			playername = data.getString("PlayerName");
			uuid = data.getString("UUID");
		}
		
		race = EnumRace.getByName(data.getString("Race"));
	}
	
	public NBTTagCompound getNBT() {
		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		}
		NBTTagCompound compound = new NBTTagCompound();
		levelData.saveNBTData(compound);
		spellData.saveNBTData(compound);
		
		compound.setString("PlayerName", playername);
		compound.setString("UUID", uuid);
		if (race != null) {
			compound.setString("Race", race.name());
		} else {
			compound.setString("Race", EnumRace.REGULAR.name());
		}
		
		return compound;
	}

	@Override
	public void init(Entity entity, World world) {
		
	}

}
