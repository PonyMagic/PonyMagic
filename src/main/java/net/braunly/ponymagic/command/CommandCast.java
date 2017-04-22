package net.braunly.ponymagic.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.race.EnumRace;
import net.braunly.ponymagic.spells.Spell;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CommandCast extends CommandBase 
{	
	@Override
	public List getCommandAliases()
	{
		List aliases = new ArrayList();
	    aliases.add("cast");
	    return aliases;
	}
	
	@Override
	public String getCommandName() {
		return "cast";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "USAGE"; // TODO
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (commandSender instanceof EntityPlayer) {
			
			EntityPlayerMP player = (EntityPlayerMP) commandSender;
			
			if (args.length < 1) {
				throw new WrongUsageException("commands.cast.usage", new Object[0]);
			}
			
			String spellName = args[0].toLowerCase();
			
			PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
			EnumRace playerRace = playerData.race;
			
			if (playerRace != null && playerRace != EnumRace.REGULAR) {
				if (Arrays.asList(playerRace.getSpells()).contains(spellName)) {
					int spellLevel = playerData.skillData.getSkillLevel(spellName);
					if (playerData.skillData.isSkillLearned(spellName)) {
						boolean status = ((Spell) PonyMagic.spells.get(spellName)).cast((EntityPlayer)player, spellLevel);
						
						if (status) {
							player.addChatComponentMessage(new ChatComponentText("Успешно")); // TODO lang file
						} else {
							player.addChatComponentMessage(new ChatComponentText("Не удачно!")); // TODO lang file
						}
					} else {
						player.addChatComponentMessage(new ChatComponentText("Заклинание не изучено!")); // TODO lang file
					}
				} else {
					player.addChatComponentMessage(new ChatComponentText("Твоя раса не может использовать это заклинание!")); // TODO lang
				}
			} else {
				player.addChatComponentMessage(new ChatComponentText("Ты не имеешь расы!")); // TODO lang
			}
			
		} else {
			System.out.println("NONE");
		}
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2){
		return null;
	}
	
	private void similarSpells(EntityPlayerMP player, String spellName) {
		player.addChatComponentMessage(new ChatComponentText("Spell " + spellName + " not found!")); // TODO
	}
		
}