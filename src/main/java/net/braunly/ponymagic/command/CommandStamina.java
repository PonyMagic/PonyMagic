package net.braunly.ponymagic.command;

import java.util.ArrayList;
import java.util.List;

import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandStamina extends CommandBase 
{

	private EntityPlayerMP player;
	private StaminaPlayer props;
	
	@Override
	public List getCommandAliases()
	{
		List aliases = new ArrayList();
	    aliases.add("stamina");
	    return aliases;
	}
	
	@Override
	public String getCommandName() {
		return "stamina";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "commands.stamina.usage";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (commandSender instanceof EntityPlayer) {
			if (args.length < 1) {
				throw new WrongUsageException("commands.stamina.usage", new Object[0]);
			}
			
			StaminaType type = null;
			
			if (args[0].equalsIgnoreCase("check")){
				if(args.length == 1) {
					player = getCommandSenderAsPlayer(commandSender);
				
				} else if (args.length == 2) {
					player = getPlayer(commandSender, args[1]);
				
				} else {
					throw new WrongUsageException("commands.stamina.check.usage", new Object[0]);
				}
				props = getProps();
				func_152373_a(commandSender, this, "commands.stamina.check.success", new Object[] {player.getCommandSenderName(), props.getStaminaValue(StaminaType.CURRENT), props.getStaminaValue(StaminaType.MAXIMUM)});
				
			} else if (args[0].equalsIgnoreCase("zero") || args[0].equalsIgnoreCase("empty")) {
				if(args.length == 1) {
					player = getCommandSenderAsPlayer(commandSender);
				
				} else if (args.length == 2) {
					player = getPlayer(commandSender, args[1]);
				
				} else {
					throw new WrongUsageException("commands.stamina.zero.usage", new Object[0]);
				}
				props = getProps();
				props.zero();
				func_152373_a(commandSender, this, "commands.stamina.zero.success", new Object[] {player.getCommandSenderName()});
				
			} else if (args[0].equalsIgnoreCase("fill") || args[0].equalsIgnoreCase("restore")) {
				if (args.length == 1) {
					player = getCommandSenderAsPlayer(commandSender);
			
				} else if (args.length == 2) {
					player = getPlayer(commandSender, args[1]);
				
				} else {
					throw new WrongUsageException("commands.stamina.fill.usage", new Object[0]);
				}
				props = getProps();
				props.fill();
				func_152373_a(commandSender, this, "commands.stamina.fill.success", new Object[] {player.getCommandSenderName()});			
				
			} else if (args[0].equals("set")) {
				if (args.length < 2) {
					throw new WrongUsageException("commands.stamina.set.usage", new Object[0]);
				}
				
				float value = 0;
				
				if (args.length == 2) {
					player = getCommandSenderAsPlayer(commandSender);			
					value = (float) parseDoubleWithMin(commandSender, args[1], 0);
				
				} else if (args.length == 3) {
					player = getPlayer(commandSender, args[1]);			
					value = (float) parseDoubleWithMin(commandSender, args[2], 0);
			
				} else {
					throw new WrongUsageException("commands.stamina.set.usage", new Object[0]);
				}
				
				props = getProps();
				props.set(StaminaType.CURRENT, value);
				func_152373_a(commandSender, this, "commands.stamina.set.success", new Object[] {value, player.getCommandSenderName()});
		
			} else if (args[0].equals("setmax")) {
				if (args.length < 2) {
					throw new WrongUsageException("commands.stamina.setmax.usage", new Object[0]);
				}
				
				float value = 0;
				
				if (args.length == 2) {
					player = getCommandSenderAsPlayer(commandSender);			
					value = (float) parseDoubleWithMin(commandSender, args[1], 0);
					
				} else if (args.length == 3) {
					player = getPlayer(commandSender, args[1]);			
					value = (float) parseDoubleWithMin(commandSender, args[2], 0);
					
				} else {
					throw new WrongUsageException("commands.stamina.setmax.usage", new Object[0]);
				}
				props = getProps();
				props.set(StaminaType.MAXIMUM, value);
				func_152373_a(commandSender, this, "commands.stamina.setmax.success", new Object[] {value, player.getCommandSenderName()});
				
			} else if (args[0].equals("add")) {
				if (args.length < 2) {
					throw new WrongUsageException("commands.stamina.add.usage", new Object[0]);
				}
				
				float value = 0;
				
				if (args.length == 2) {
					player = getCommandSenderAsPlayer(commandSender);	
					value = (float) parseDouble(commandSender, args[1]);
					
				} else if (args.length == 3) {
					player = getPlayer(commandSender, args[1]);		
					value = (float) parseDouble(commandSender, args[2]);
					
				} else {
					throw new WrongUsageException("commands.stamina.add.usage", new Object[0]);
				}
				props = getProps();
				props.add(StaminaType.CURRENT, value);
				func_152373_a(commandSender, this, "commands.stamina.add.success", new Object[] {value, props.getStaminaValue(StaminaType.CURRENT), player.getCommandSenderName()});
				
			} else {
				throw new WrongUsageException("commands.stamina.usage", new Object[0]);
			}
		}
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2){
		if(var2.length == 1){
			ArrayList<String> completions = new ArrayList<String>();
			String[] commands = {"add", "check", "empty", "fill", "freeze", "restore", "set", "setmax", "zero"};
			for (String c : commands){
				if (c.startsWith(var2[0])) completions.add(c);
			}
			
			return completions;
			
		} else if(var2.length == 2){
			ArrayList<String> completions = new ArrayList<String>();
			EntityPlayer player = getCommandSenderAsPlayer(var1);
			for (Object o : player.worldObj.playerEntities){
				EntityPlayer p = (EntityPlayer)o;
				if (p.getCommandSenderName().toLowerCase().startsWith(var2[1].toLowerCase())) {
					completions.add(p.getCommandSenderName());
				}
			}
			return completions;
		}
		return null;
	}
	
	protected StaminaPlayer getProps() {
		props = StaminaPlayer.get((EntityPlayer)player);
		if (props == null) throw new CommandException("commands.stamina.playerHasNoProps", new Object[] {player.getCommandSenderName()});
		return props;
	}
		
}