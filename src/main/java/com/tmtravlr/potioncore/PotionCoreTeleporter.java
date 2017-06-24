package com.tmtravlr.potioncore;

import java.util.Iterator;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

/**
 * Used to teleport between dimensions.
 * Pretty much copied from Colourful Portals code.
 * @author Rebeca (Tmtravlr)
 * @Date June 2015
 */
public class PotionCoreTeleporter extends Teleporter
{
	private WorldServer worldServer;
	
	private double x;
	private double y;
	private double z;
	
	private float pitch;
	private float yaw;
	
	private double motionX;
	private double motionY;
	private double motionZ;

	public PotionCoreTeleporter(WorldServer worldServerOld, double xToSet, double yToSet, double zToSet, float pitchToSet, float yawToSet, 
			double motionXToSet, double motionYToSet, double motionZToSet) {

		super(worldServerOld);
		this.worldServer = worldServerOld;
		this.x = xToSet;
		this.y = yToSet;
		this.z = zToSet;
		this.pitch = pitchToSet;
		this.yaw = yawToSet;
		this.motionX = motionXToSet;
		this.motionY = motionYToSet;
		this.motionZ = motionZToSet;
	}
	
	public PotionCoreTeleporter(WorldServer worldServerOld, double xToSet, double yToSet, double zToSet, float pitchToSet, float yawToSet) {
		this(worldServerOld, xToSet, yToSet, zToSet, pitchToSet, yawToSet, 0, 0, 0);
	}

	public void placeInPortal(Entity entity, double posX, double posY, double posZ, float par8) {

		entity.setLocationAndAngles(this.x, this.y, this.z, this.yaw, this.pitch);
		entity.setRotationYawHead(this.yaw);
		entity.motionX = this.motionX;
		entity.motionY = this.motionY;
		entity.motionZ = this.motionZ;
		
		if(entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			player.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
		}
	}
	
	//Begin static helper methods:
	
	public static void teleportEntity(Entity entity, World newWorld, double x, double y, double z) {
		
		teleportEntity(entity, newWorld, x, y, z, entity.rotationPitch, entity.rotationYaw, 0, 0, 0);
	}
	
	public static void teleportEntity(Entity entity, World newWorld, double x, double y, double z, float pitch, float yaw) {
		
		teleportEntity(entity, newWorld, x, y, z, pitch, yaw, 0, 0, 0);
	}
	
	public static Entity teleportEntity(Entity entity, World newWorld, double x, double y, double z, float pitch, float yaw, 
			double motionX, double motionY, double motionZ) {
		int dimension = newWorld.provider.dimensionId;
		int currentDimension = entity.worldObj.provider.dimensionId;
		if (dimension != currentDimension)
		{
			return transferEntityToDimension(entity, dimension, x, y, z, pitch, yaw, motionX, motionY, motionZ);
		}
		
		entity.setLocationAndAngles(x, y, z, yaw, pitch);
		entity.setRotationYawHead(yaw);
		entity.motionX = motionX;
		entity.motionY = motionY;
		entity.motionZ = motionZ;
		
		return entity;
	}
	
	public static void teleportPlayer(EntityPlayerMP player, World newWorld, double x, double y, double z) {
		
		teleportPlayer(player, newWorld, x, y, z, player.rotationPitch, player.rotationYaw, 0, 0, 0);
	}

	public static void teleportPlayer(EntityPlayerMP player, World newWorld, double x, double y, double z, float pitch, float yaw) {
		
		teleportPlayer(player, newWorld, x, y, z, pitch, yaw, 0, 0, 0);
	}
	
	public static void teleportPlayer(EntityPlayerMP player, World newWorld, double x, double y, double z, float pitch, float yaw, 
			double motionX, double motionY, double motionZ) {
		
		int dimension = newWorld.provider.dimensionId;
		int currentDimension = player.worldObj.provider.dimensionId;
		if (currentDimension != dimension)
		{
			if (!newWorld.isRemote) {
				if (currentDimension != 1) {
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension, new PotionCoreTeleporter(player.mcServer.worldServerForDimension(dimension), x, y, z, pitch, yaw, motionX, motionY, motionZ));
				} else {
					forceTeleportPlayerFromEnd(player, dimension, new PotionCoreTeleporter(player.mcServer.worldServerForDimension(dimension), x, y, z, pitch, yaw, motionX, motionY, motionZ));
				}
				player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));				
			}
		}
		else {
			
			player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
			
			player.motionX = motionX;
			player.motionY = motionY;
			player.motionZ = motionZ;
			player.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
			
			newWorld.updateEntityWithOptionalForce(player, false);
		}
	}
	
	//Private stuff; This may not work if called without the stuff above.
	
	private static void forceTeleportPlayerFromEnd(EntityPlayerMP player, int newDimension, Teleporter teleporter)
	{
		int j = player.dimension;
		WorldServer worldServerOld = player.mcServer.worldServerForDimension(player.dimension);
		player.dimension = newDimension;
		WorldServer worldServerNew = player.mcServer.worldServerForDimension(player.dimension);
		player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
		worldServerOld.removePlayerEntityDangerously(player);
		player.isDead = false;

		WorldProvider pOld = worldServerOld.provider;
		WorldProvider pNew = worldServerNew.provider;
		double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
		double d0 = player.posX * moveFactor;
		double d1 = player.posZ * moveFactor;
		float f = player.rotationYaw;

		worldServerOld.theProfiler.startSection("placing");
		d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
		d1 = MathHelper.clamp_int((int)d1, -29999872, 29999872);
		if (player.isEntityAlive())
		{
			player.setLocationAndAngles(d0, player.posY, d1, player.rotationYaw, player.rotationPitch);
			teleporter.placeInPortal(player, player.posX, player.posY, player.posZ, f);
			worldServerNew.spawnEntityInWorld(player);
			worldServerNew.updateEntityWithOptionalForce(player, false);
		}
		worldServerOld.theProfiler.endSection();

		player.setWorld(worldServerNew);

		player.mcServer.getConfigurationManager().func_72375_a(player, worldServerOld);
		player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.theItemInWorldManager.setWorld(worldServerNew);
		player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, worldServerNew);
		player.mcServer.getConfigurationManager().syncPlayerInventory(player);
		Iterator iterator = player.getActivePotionEffects().iterator();
		while (iterator.hasNext())
		{
			PotionEffect potioneffect = (PotionEffect)iterator.next();
			player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, j, newDimension);
	}

	private static Entity transferEntityToDimension(Entity toTeleport, int newDimension, double x, double y, double z, float pitch, float yaw, 
			double motionX, double motionY, double motionZ) {
		
		if (!toTeleport.isDead)
		{
			toTeleport.worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int oldDimension = toTeleport.dimension;
			WorldServer worldServerOld = minecraftserver.worldServerForDimension(oldDimension);
			WorldServer worldServerNew = minecraftserver.worldServerForDimension(newDimension);
			toTeleport.dimension = newDimension;

			toTeleport.worldObj.removeEntity(toTeleport);
			toTeleport.isDead = false;
			toTeleport.worldObj.theProfiler.startSection("reposition");
			if (oldDimension == 1) {
				forceTeleportEntityFromEnd(toTeleport, newDimension, new PotionCoreTeleporter(worldServerOld, x, y, z, pitch, yaw, motionX, motionY, motionZ), worldServerNew);
			} else {
				minecraftserver.getConfigurationManager().transferEntityToWorld(toTeleport, oldDimension, worldServerOld, worldServerNew, new PotionCoreTeleporter(worldServerOld, x, y, z, pitch, yaw, motionX, motionY, motionZ));
			}
			toTeleport.worldObj.theProfiler.endStartSection("reloading");
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(toTeleport), worldServerNew);
			if (entity != null)
			{
				entity.copyDataFrom(toTeleport, true);
				worldServerNew.spawnEntityInWorld(entity);
			}
			toTeleport.isDead = true;
			toTeleport.worldObj.theProfiler.endSection();
			worldServerOld.resetUpdateEntityTick();
			worldServerNew.resetUpdateEntityTick();
			toTeleport.worldObj.theProfiler.endSection();

			return entity;
		}
		return toTeleport;
	}

	private static void forceTeleportEntityFromEnd(Entity entity, int newDimension, Teleporter teleporter, WorldServer worldServerNew)
	{
		worldServerNew.spawnEntityInWorld(entity);
		entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		worldServerNew.updateEntityWithOptionalForce(entity, false);
		teleporter.placeInPortal(entity, entity.posX, entity.posY, entity.posZ, 0.0F);

		entity.setWorld(worldServerNew);
	}
}