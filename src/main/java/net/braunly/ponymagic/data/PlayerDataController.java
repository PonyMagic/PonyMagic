package net.braunly.ponymagic.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.util.NBTJsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PlayerDataController {
	public static PlayerDataController instance;

	public PlayerDataController() {
		instance = this;
	}

	public String hasPlayer(String username) {
		for (String name : getUsernameData().keySet()) {
			if (name.equalsIgnoreCase(username))
				return name;
		}

		return "";
	}

	public PlayerData getPlayerData(EntityPlayer player) {
		return getDataFromUsername(PonyMagic.Server, player.getName());
	}

	public PlayerData getDataFromUsername(MinecraftServer server, String username) {
		EntityPlayer player = server.getPlayerList().getPlayerByUsername(username);
		PlayerData data = null;
		if (player == null) {
			Map<String, NBTTagCompound> map = getUsernameData();
			for (String name : map.keySet()) {
				if (name.equalsIgnoreCase(username)) {
					data = new PlayerData();
					data.setNBT(map.get(name));
					break;
				}
			}
		} else
			data = PlayerData.get(player);

		return data;
	}

	public Map<String, NBTTagCompound> getUsernameData() {
		Map<String, NBTTagCompound> map = new HashMap<String, NBTTagCompound>();
		for (File file : getWorldSaveDirectory().listFiles()) {
			if (file.isDirectory() || !file.getName().endsWith(".json"))
				continue;
			try {
				NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
				if (compound.hasKey("PlayerName")) {
					map.put(compound.getString("PlayerName"), compound);
				}
			} catch (Exception e) {
				PonyMagic.log.error("Error loading: " + file.getAbsolutePath(), e);
			}
		}
		return map;
	}

	public List<PlayerData> getPlayersData(ICommandSender sender, String username) {
		ArrayList<PlayerData> list = new ArrayList<PlayerData>();
		List<EntityPlayerMP> players;
		try {
			players = EntitySelector.matchEntities(sender, username, EntityPlayerMP.class);
			
			if (players.isEmpty()) {
				PlayerData data = getDataFromUsername(sender.getServer(), username);
				if (data != null)
					list.add(data);
			} else {
				for (EntityPlayer player : players) {
					list.add(PlayerData.get(player));
				}
			}
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static File getWorldSaveDirectory() {
		try {
			if (PonyMagic.Server == null)
				return null;
			File saves = new File(".");
			if (!PonyMagic.Server.isDedicatedServer()) {
				saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
			}
			File savedir = new File(new File(saves, PonyMagic.Server.getFolderName()), PonyMagic.MODID);
			if (!savedir.exists()) {
				savedir.mkdir();
			}
			return savedir;

		} catch (Exception e) {
			PonyMagic.log.error("Error getting worldsave", e);
		}
		return null;
	}
}
