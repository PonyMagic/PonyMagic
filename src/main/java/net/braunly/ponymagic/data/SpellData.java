package net.braunly.ponymagic.data;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SpellData {
	public HashMap<String,Integer> spellData = new HashMap<String,Integer>();
	
	public void loadNBTData(NBTTagCompound compound) {
		HashMap<String,Integer> spellData = new HashMap<String,Integer>();
		
		if(compound == null)
			return;
        
		NBTTagList list = compound.getTagList("Spells", 10);
        if(list == null){
        	return;
        }
        
        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            spellData.put(nbttagcompound.getString("Name"),nbttagcompound.getInteger("Level"));
        }
        this.spellData = spellData;
	}
	
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(String spellName : spellData.keySet()){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", spellName);
			nbttagcompound.setInteger("Level", getSpellLevel(spellName));
			list.appendTag(nbttagcompound);
		}
		
		compound.setTag("Spells", list);
	}
	
	public int getSpellLevel(String name) {
		if(!spellData.containsKey(name)){
			spellData.put(name, 0);
			return 0;
		}
		return spellData.get(name);
	}

	public void upLevel(String name) {
		spellData.put(name, getSpellLevel(name) + 1);
	}
}
