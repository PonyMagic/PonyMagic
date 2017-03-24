package com.tmtravlr.potioncore.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SToCMessage implements IMessage{
	private byte[] data;
	
	public SToCMessage() 
	{
		this(new byte[]{0});
	}
	
	public SToCMessage(ByteBuf dataToSet)
    {
        this(dataToSet.array());
    }

    public SToCMessage(byte[] dataToSet)
    {
		if (dataToSet.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
        
        this.data = dataToSet;

    }

    /**
     * Deconstruct your message into the supplied byte buffer
     * @param buf
     */
	@Override
	public void toBytes(ByteBuf buffer) {
		//System.out.println("Encoding General Packet!");
        
		if (data.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		
        buffer.writeShort(this.data.length);
        buffer.writeBytes(this.data);
		
	}

	/**
     * Convert from the supplied buffer into your specific message type
     * @param buffer 
     */
	@Override
	public void fromBytes(ByteBuf buffer) {
		
		this.data = new byte[buffer.readShort()];
        buffer.readBytes(this.data);
	}
	
    public byte[] getData() {
        return this.data;
    }
}
