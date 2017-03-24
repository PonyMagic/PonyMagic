package net.braunly.ponymagic.entity.player;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.network.packets.FreezePacket;
import net.braunly.ponymagic.network.packets.TotalStaminaPacket;
import net.braunly.ponymagic.util.Queue;
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
	public final static float DEFAULT_STAMINA = Config.defaultStamina;
	
	private final EntityPlayer player;
	private final DataWatcher dw;
	
	private float stamina;
	private float current;
	private float max;
	private boolean fly = false;
	
	private boolean updateCurrent = true;
	private boolean updateMaximum = true;
	
	private final Queue currentStaminaQueue = new Queue();
	private final Queue maximumStaminaQueue = new Queue();
	
	public static enum StaminaType {
		STAMINA(20),
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
		
		this.stamina = DEFAULT_STAMINA;
		
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

		properties.setFloat("stamina", stamina);
		
		properties.setFloat("current", this.dw.getWatchableObjectFloat(StaminaType.CURRENT.getMeta()));
		properties.setFloat("max", this.dw.getWatchableObjectFloat(StaminaType.MAXIMUM.getMeta()));
		
		compound.setTag(NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(NAME);
		
		this.stamina = properties.getFloat("stamina");
		
		this.dw.updateObject(StaminaType.CURRENT.getMeta(), properties.getFloat("current"));
		this.dw.updateObject(StaminaType.MAXIMUM.getMeta(), properties.getFloat("max"));
	}
	
	
	public void addToQueue(StaminaType type, float amount) {
		if(type == null) throw new NullPointerException("Type must not be null.");
		switch(type) {
			case CURRENT:
				currentStaminaQueue.add(amount);
				break;
			case MAXIMUM:
				maximumStaminaQueue.add(amount);
				break;
			default:
				throw new IllegalArgumentException("Type must be current or maximum.");
		}
	}
	
	
	public void zero() {
		set(StaminaType.CURRENT, 0.0F);
	}
	
	public void fill() {
		set(StaminaType.CURRENT, getStaminaValue(StaminaType.STAMINA));
	}
	
	public void reset(StaminaType type) {
		if (type == null) throw new NullPointerException("Type must not be null.");
		switch(type) {
			case CURRENT:
				currentStaminaQueue.reset();;
				break;
			case MAXIMUM:
				maximumStaminaQueue.reset();;
				break;
			default:
				throw new IllegalArgumentException("Type must be current, maximum.");
		}
	}
	
	public void set(StaminaType type, float amount) {
		if (type.equals(StaminaType.STAMINA)) {
			stamina = amount;
			if (!player.worldObj.isRemote) {
				PonyMagic.channel.sendTo(new TotalStaminaPacket(amount), (EntityPlayerMP) player);
			}
		}
		this.dw.updateObject(type.getMeta(), amount);
	}
	
	public void setFrozen(StaminaType type, boolean freeze) {
		switch (type) {
			case CURRENT:
				updateCurrent = !freeze;
				break;
			case MAXIMUM:
				updateMaximum = !freeze;
				break;
			case STAMINA:
				updateMaximum = !freeze;
				updateMaximum = !freeze;
				break;
			default:
				throw new NullPointerException("Type must not be null");	
		}
		if (!player.worldObj.isRemote) {
			PonyMagic.channel.sendTo(new FreezePacket(type, freeze), (EntityPlayerMP) player);
		}
	}
	
	public void update() {
		float stamina = getStaminaValue(StaminaType.STAMINA);
		float current = getStaminaValue(StaminaType.CURRENT);
		float maximum = getStaminaValue(StaminaType.MAXIMUM);
		
		if (updateCurrent) current += currentStaminaQueue.getNetChange();
		if (updateMaximum) maximum += maximumStaminaQueue.getNetChange();
		
		if(maximum >= stamina) maximum = stamina;
		if(maximum <= 0.0F) maximum = 0.0F;
		
		if(current >= maximum) current = maximum;
		if(current <= 0.0F) current = 0.0F;
		
		this.dw.updateObject(StaminaType.CURRENT.getMeta(), current);
		this.dw.updateObject(StaminaType.MAXIMUM.getMeta(), maximum);
	}	
	
	public float getStaminaValue(StaminaType type) {
		switch(type) {
			case STAMINA:
				 return stamina;
			case CURRENT:
				return this.dw.getWatchableObjectFloat(StaminaType.CURRENT.getMeta());
			case MAXIMUM:
				return this.dw.getWatchableObjectFloat(StaminaType.MAXIMUM.getMeta());
			default:
				throw new NullPointerException("Type must not be null.");
		}
	}
	
	public boolean getFly(){
		return this.fly;
	}
	
	public void setFly(boolean fly){
		this.fly = fly;
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
}
