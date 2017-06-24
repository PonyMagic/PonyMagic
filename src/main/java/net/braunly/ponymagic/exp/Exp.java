package net.braunly.ponymagic.exp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.LevelData;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.event.LevelUpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class Exp {
	
//	public static ArrayList<Integer> lvlExp = new ArrayList<Integer>();
	
	public void load() {
		try {
			String line;
			
//			URL lvlTable = new URL("https://docs.google.com/spreadsheets/d/1cnrFHQHQ2xgITkFdIohpT8v3tziTJb_JaUwhZTs5s4U/gviz/tq?tqx=out:csv&sheet=mod_lvl");
//			BufferedReader lvlBuf = new BufferedReader(new InputStreamReader(lvlTable.openStream()));
//			while ((line = lvlBuf.readLine()) != null) {
//				line = line.replaceAll("\"", "");
//				String[] lvlLine = line.split(",", -1);
//				
//				Exp.lvlExp.add(Integer.parseInt(lvlLine[1]));
//			}

			URL expTable = new URL("https://docs.google.com/spreadsheets/d/1cnrFHQHQ2xgITkFdIohpT8v3tziTJb_JaUwhZTs5s4U/gviz/tq?tqx=out:csv&sheet=mod_exp");
			BufferedReader expBuf = new BufferedReader(new InputStreamReader(expTable.openStream()));
			while ((line = expBuf.readLine()) != null) {
				line = line.replaceAll("\"", "");
				String[] expLine = line.split(",", -1);
				String mineBlockId = checkEmpty(expLine[0]);
				String mineBlockExp = checkEmpty(expLine[3]);
				String placeBlockId = checkEmpty(expLine[4]);
				String placeBlockExp = checkEmpty(expLine[7]);
				String craftId = checkEmpty(expLine[8]);
				String craftExp = checkEmpty(expLine[11]);
				String killName = checkEmpty(expLine[12]);
				String killExp = checkEmpty(expLine[15]);
				
				MineBlock.exp.put(Integer.parseInt(mineBlockId), Double.parseDouble(mineBlockExp));
				PlaceBlock.exp.put(Integer.parseInt(placeBlockId), Double.parseDouble(placeBlockExp));
				CraftItem.exp.put(Integer.parseInt(craftId), Double.parseDouble(craftExp));
				KillEntity.exp.put(killName, Double.parseDouble(killExp));
			}
			
	    	PonyMagic.log.info("Experience loaded!");
	    	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
	    	PonyMagic.log.error("Can't parse experience table!");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			PonyMagic.log.error("Can't parse experience table!");
		} catch (IOException e) {
			e.printStackTrace();
			PonyMagic.log.error("Can't parse experience table!");
		}
	}
	
	public static void addExp(EntityPlayer player, Double expCount) {
		if (expCount == null)
			return;
		PlayerData data = PlayerDataController.instance.getPlayerData(player);
		LevelData levelData = data.levelData;
		
		PonyMagic.log.info(expCount);
		
		if (levelData.getLevel() < PonyMagic.MAX_LVL) {
			if (Config.expModifier) {
				expCount *= Config.expModifierAmount;
			}
			levelData.addExp(expCount);
			
			if (levelData.getExp() >= (levelData.getLevel() + 1) * Config.expPerLevel) {
				MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, levelData.getLevel()+1));
				levelData.addLevel();
				levelData.resetExp();
			}
			if (levelData.getLevel() % 3 == 0) {
				levelData.addFreeSkillPoints();
			}
		}
		
		data.saveNBTData(null);
	}
	
	private String checkEmpty(String str) {
		if (str.isEmpty()) {
			return "0";
		} else {
			return str;
		}
	}
}
