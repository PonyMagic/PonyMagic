package net.braunly.ponymagic.data;

import me.braunly.ponymagic.api.interfaces.ITickDataStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TickData implements ITickDataStorage {
    private HashMap<String, Integer> timers = new HashMap<>();

    @Override
    public int getTicks(String skillName) {
        if (!this.timers.containsKey(skillName)) {
            return 0;
        }
        return this.timers.get(skillName);
    }

    @Override
    public HashMap<String, Integer> getTimers() {
        return this.timers;
    }

    @Override
    public boolean isTicking(String skillName) {
        return this.timers.containsKey(skillName);
    }
    @Override
    public boolean isTicking() {
        return !this.timers.isEmpty();
    }

    @Override
    public void startTicking(String skillname, int ticks) {
        this.timers.put(skillname, ticks * 2);  // Timer ticks twice per tick. I dunno why
    }

    @Override
    public void stopTicking(String skillname) {
        this.timers.remove(skillname);
    }

    @Override
    public void tick() {
        Iterator<Map.Entry<String, Integer>> entryIt = this.timers.entrySet().iterator();

        while (entryIt.hasNext()) {
            Map.Entry<String, Integer> entry = entryIt.next();
            if (entry.getValue() <= 0) {
                entryIt.remove();
            } else {
                entry.setValue(entry.getValue() - 1);
            }
        }
    }

    @Override
    public void reset() {
        this.timers.clear();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        HashMap<String, Integer> timersNew = new HashMap<>();

        if (compound == null)
            return;

        NBTTagList list = compound.getTagList("Timers", 10);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            timersNew.put(nbttagcompound.getString("Name"), nbttagcompound.getInteger("Ticks"));
        }
        this.timers = timersNew;
    }

    @Override
    public void saveToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        this.timers.forEach((skillName, ticks) -> {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Name", skillName);
            nbttagcompound.setInteger("Ticks", ticks);
            list.appendTag(nbttagcompound);
        });

        compound.setTag("Timers", list);
    }
}
