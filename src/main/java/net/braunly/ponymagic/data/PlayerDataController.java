package net.braunly.ponymagic.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Timer;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.util.NBTJsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import static com.codahale.metrics.MetricRegistry.name;

public class PlayerDataController {
	public static PlayerDataController instance;
	private final Timer getPlayerDataTimer = PonyMagic.METRICS.timer(name(PlayerDataController.class, "getPlayerData"));

	public PlayerDataController() {
		instance = this;
	}

	public PlayerData getPlayerData(EntityPlayer player) {
		return getDataFromUsername(PonyMagic.Server, player.getName());
	}

	public PlayerData getDataFromUsername(MinecraftServer server, String username) {
		final Timer.Context context = getPlayerDataTimer.time();
		PlayerData data = null;
		try {
			EntityPlayer player = server.getPlayerList().getPlayerByUsername(username);
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
		} finally {
			context.stop();
		}
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
