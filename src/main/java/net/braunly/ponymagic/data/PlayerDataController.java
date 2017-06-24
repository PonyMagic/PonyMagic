package net.braunly.ponymagic.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.util.LogWriter;
import net.braunly.ponymagic.util.NBTJsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PlayerDataController {		
	public static PlayerDataController instance;
	
	public PlayerDataController(){
		instance = this;
	}
	public File getSaveDir(){
		try{
			File file = new File(this.getWorldSaveDirectory(),"playerdata");
			if(!file.exists())
				file.mkdir();
			return file;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public NBTTagCompound loadPlayerData(String player){
		File saveDir = getSaveDir();
		String filename = player;
		if(filename.isEmpty())
			filename = "noplayername";
		filename += ".json";
		try {
	        File file = new File(saveDir, filename);
	        if(file.exists()){
		        return NBTJsonUtil.LoadFile(file);
	        }
		} catch (Exception e) {
			LogWriter.error("Error loading: " + filename, e);
		}

		return new NBTTagCompound();
	}
	
	public void savePlayerData(PlayerData data){
		NBTTagCompound compound = data.getNBT();
		String filename = data.uuid + ".json";
		try {
			File saveDir = getSaveDir();
            File file = new File(saveDir, filename+"_new");
            File file1 = new File(saveDir, filename);
            NBTJsonUtil.SaveFile(file, compound);
            if(file1.exists()){
                file1.delete();
            }
            file.renameTo(file1);
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}
	
	public PlayerData getPlayerData(EntityPlayer player){
		PlayerData data = (PlayerData) player.getExtendedProperties("PonyMagicData");
		if(data == null){
			player.registerExtendedProperties("PonyMagicData", data = new PlayerData());
			data.player = player;
			data.loadNBTData(null);
		}
		data.player = player;
		return data;
	}
	public String hasPlayer(String username) {
        for(String name : getUsernameData().keySet()){
        	if(name.equalsIgnoreCase(username))
        		return name;
        }
		
		return "";
	}
	
	public PlayerData getDataFromUsername(String username){
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(username);
		PlayerData data = null;
		if(player == null){
			Map<String, NBTTagCompound> map = getUsernameData();
			for(String name : map.keySet()){
				if(name.equalsIgnoreCase(username)){
					data = new PlayerData();
					data.setNBT(map.get(name));
					break;
				}
			}
		}
		else
			data = getPlayerData(player);
		
		return data;
	}
	
	public Map<String, NBTTagCompound> getUsernameData(){
		Map<String, NBTTagCompound> map = new HashMap<String, NBTTagCompound>();
        for(File file : getSaveDir().listFiles()){
        	if(file.isDirectory() || !file.getName().endsWith(".json"))
        		continue;
			try {
				NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
	        	if(compound.hasKey("PlayerName")){
	        		map.put(compound.getString("PlayerName"), compound);
	        	}
			} catch (Exception e) {
				LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
			}
        }
		return map;
	}

	public static File getWorldSaveDirectory() {
        MinecraftServer server = MinecraftServer.getServer();
        File saves = new File(".");
        if (server != null && !server.isDedicatedServer()) {
            saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
        }
        if (server != null) {
            File savedir = new File(new File(saves, server.getFolderName()), PonyMagic.MODID);
            if (!savedir.exists()) {
                savedir.mkdir();
            }
            return savedir;
        }
        return null;
    }
}
