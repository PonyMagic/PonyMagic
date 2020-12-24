package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.events.LevelUpEvent;
import me.braunly.ponymagic.api.interfaces.IPlayerDataController;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.LevelConfig;
import net.braunly.ponymagic.util.NBTJsonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;


public class PlayerDataController implements IPlayerDataController {
	private MinecraftServer server;

	public PlayerDataController(MinecraftServer server) {
		this.server = server;
	}

	public IPlayerDataStorage getPlayerData(String playerName) {
		EntityPlayer player = this.server.getPlayerList().getPlayerByUsername(playerName);
		return this.getPlayerData(player);
	}

	@Override
	public IPlayerDataStorage getPlayerData(EntityPlayer player) {
        IPlayerDataStorage data = PonyMagicAPI.getPlayerDataStorage(player);
        if (data.getPlayer() == null) {
            data.setPlayer(player);

            NBTTagCompound compound = loadPlayerData(data.getUUID());
            data.setNBT(compound);
        }
        return data;
	}

    @Override
	public void savePlayerData(IPlayerDataStorage data) {
		final String filename = data.getUUID() + ".json";

		if (data.getLevelData().isLevelUp()) {
			data.getLevelData().levelUp();
			int nextLevel = data.getLevelData().getLevel() + 1;

			if (nextLevel <= PonyMagic.MAX_LVL) {
				data.getLevelData().setGoals(LevelConfig.getRaceLevelConfig(data.getRace(), nextLevel).getQuestsWithGoals());
			}
			MinecraftForge.EVENT_BUS.post(new LevelUpEvent(

					data.getPlayer(),
					data.getLevelData().getLevel()
			));
		}

		final NBTTagCompound compound = data.getNBT();

		try {
			File saveDir = this.getWorldSaveDirectory();
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

    private NBTTagCompound loadPlayerData(String uuid) {
        File saveDir = this.getWorldSaveDirectory();
        if (!(uuid.isEmpty())) {
            uuid += ".json";
            try {
                File file = new File(saveDir, uuid);
                if (file.exists()) {
                    return NBTJsonUtil.LoadFile(file);
                }
            } catch (Exception e) {
                PonyMagic.log.error("Error loading player data from : " + uuid);
                PonyMagic.log.catching(e);
            }
        }

        return new NBTTagCompound();
    }

    private File getWorldSaveDirectory() {
		try {
			if (this.server == null)
				return null;
			File saves = new File(".");
			if (!this.server.isDedicatedServer()) {
				saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
			}
			File savedir = new File(new File(saves, this.server.getFolderName()), PonyMagic.MODID);
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
