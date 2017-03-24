package net.braunly.ponymagic.util;

import net.braunly.ponymagic.util.NBTJsonUtil.JsonFile;

public class JsonException extends Exception{
	public JsonException(String message, JsonFile json){
		super(message + ": " + json.getCurrentPos());
	}
}