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

public class CommandMagic extends CommandBase 
{	
	@Override
	public List getCommandAliases()
	{
		List aliases = new ArrayList();
	    aliases.add("magic");
	    return aliases;
	}
	
	@Override
	public String getCommandName() {
		return "magic";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "command.magic.usage";  // TODO
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (commandSender instanceof EntityPlayer) {
			
			EntityPlayerMP player = (EntityPlayerMP) commandSender;
			
			if (args.length < 3) {
				throw new WrongUsageException("commands.magic.usage", new Object[0]);
			}
			
			if (args[0].equalsIgnoreCase("race")) {
				String playerName = args[1];
				EnumRace race = EnumRace.getByName(args[2]);
				
				if (race != null) {
					PlayerData playerData = PlayerDataController.instance.getDataFromUsername(playerName);
					playerData.race = race;
					playerData.saveNBTData(null);
					player.addChatMessage(new ChatComponentText(playerName + " теперь - " + race.getLocalizedName()));  // TODO lang file
				} else {
					player.addChatMessage(new ChatComponentText("Неизвестная раса!")); // TODO lang file
				}
				
			} else if (args[0].equalsIgnoreCase("spell")) {
				String playerName = args[1];
				String spellName = args[2];
				
				PlayerData playerData = PlayerDataController.instance.getDataFromUsername(playerName);
				
				if (Arrays.asList(playerData.race.getSpells()).contains(spellName)) {
					playerData.skillData.upLevel(spellName);
					playerData.saveNBTData(null);
				} else {
					player.addChatComponentMessage(new ChatComponentText(playerName + " не может использовать это заклинание!")); // TODO lang
				}
			} else if (args[0].equalsIgnoreCase("test")) {
				PlayerData playerData = PlayerDataController.instance.getDataFromUsername(player.getCommandSenderName());
				playerData.race = EnumRace.ZEBRA;
				playerData.skillData.upLevel("fireresistance");
				playerData.saveNBTData(null);
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