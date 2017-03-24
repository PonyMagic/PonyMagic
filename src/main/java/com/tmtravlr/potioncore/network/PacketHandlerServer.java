package com.tmtravlr.potioncore.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerServer implements IMessageHandler<CToSMessage,IMessage> {

	//Types of packets

	public static final int CLIMB_FALL = 1;

	/**
	 * Handles Server Side Packets. Only returns null.
	 */
	@Override
	public IMessage onMessage(CToSMessage packet, MessageContext context)
	{
		ByteBuf buff = Unpooled.wrappedBuffer(packet.getData());

		int type = buff.readInt();
		
		switch(type) {
		case CLIMB_FALL: { 
			
			Entity player = getPlayerByUUID( MinecraftServer.getServer().getConfigurationManager(), new UUID(buff.readLong(), buff.readLong()));
			
			if(player != null) {
				player.fallDistance = 0.0f;
			}
			break;
		}
		default:
			//do nothing.
		}


		return null;
	}

    public EntityPlayerMP getPlayerByUUID(ServerConfigurationManager manager, UUID uuid)
    {
        Iterator iterator = manager.playerEntityList.iterator();
        EntityPlayerMP entityplayermp;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entityplayermp = (EntityPlayerMP)iterator.next();
        }
        while (!entityplayermp.func_146094_a(entityplayermp.getGameProfile()).equals(uuid));

        return entityplayermp;
    }
}
