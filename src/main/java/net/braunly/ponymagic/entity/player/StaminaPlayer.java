package net.braunly.ponymagic.entity.player;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class StaminaPlayer implements IExtendedEntityProperties
{
	public final static String NAME = "StaminaPlayer";
	public final static float DEFAULT_STAMINA = Config.defaultStaminaPool;
	
	private final EntityPlayer player;
	private final DataWatcher dw;
	
	private float current;
	private float maximum;
	
	public static enum StaminaType {
		CURRENT(21),
		MAXIMUM(22);
		
		private StaminaType(int meta) {
			this.meta = meta;
		}
		
		public int getMeta() {
			return this.meta;
		}
		
		private final int meta;
	}

	public StaminaPlayer(EntityPlayer player)
	{
		this.player = player;
		this.dw = this.player.getDataWatcher();
		
		this.dw.addObject(StaminaType.CURRENT.getMeta(), DEFAULT_STAMINA);
		this.dw.addObject(StaminaType.MAXIMUM.getMeta(), DEFAULT_STAMINA);
	}
	

	public final static void register(EntityPlayer player) {
		player.registerExtendedProperties(NAME, new StaminaPlayer(player));
	}

	public static final StaminaPlayer get(EntityPlayer player) {
		return (StaminaPlayer)player.getExtendedProperties(NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setFloat("current", this.dw.getWatchableObjectFloat(StaminaType.CURRENT.getMeta()));
		properties.setFloat("max", this.dw.getWatchableObjectFloat(StaminaType.MAXIMUM.getMeta()));
		
		compound.setTag(NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(NAME);
		
		this.dw.updateObject(StaminaType.CURRENT.getMeta(), properties.getFloat("current"));
		this.dw.updateObject(StaminaType.MAXIMUM.getMeta(), properties.getFloat("max"));
	}
	
	public void set(StaminaType type, float amount) {
		switch(type) {
			case CURRENT:
				if(amount >= getStaminaValue(StaminaType.MAXIMUM)) amount = getStaminaValue(StaminaType.MAXIMUM);
				break;
			case MAXIMUM:
				if (!player.worldObj.isRemote) {
					if(amount <= 0.0F) amount = 0.0F;
					PonyMagic.channel.sendTo(new TotalStaminaPacket(amount), (EntityPlayerMP) player);
				}
			    break;
			default:
				throw new NullPointerException("Type must not be null.");
		}
		
		this.dw.updateObject(type.getMeta(), amount);
	}
	
	public float getStaminaValue(StaminaType type) {
		switch(type) {
			case CURRENT:
				return this.dw.getWatchableObjectFloat(StaminaType.CURRENT.getMeta());
			case MAXIMUM:
				return this.dw.getWatchableObjectFloat(StaminaType.MAXIMUM.getMeta());
			default:
				throw new NullPointerException("Type must not be null.");
		}
	}
	
	public void add(StaminaType type, float amount) {
		amount = getStaminaValue(type) + amount;
		set(type, amount);
	}
	
	public boolean remove(StaminaType type, float amount) {
		if (amount > getStaminaValue(type)) return false;
		
		amount = getStaminaValue(type) - amount;
		set(type, amount);
		return true;
		
	}
	
	public void zero() {
		set(StaminaType.CURRENT, 0.0F);
	}
	
	public void fill() {
		set(StaminaType.CURRENT, getStaminaValue(StaminaType.MAXIMUM));
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
}
