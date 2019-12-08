package net.braunly.ponymagic.command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import lombok.Getter;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.capabilities.stamina.StaminaProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandStamina extends CommandBase {
	@Getter
	public final List<String> aliases = Lists.newArrayList("stamina");
	@Getter
	public final String name = "stamina";
	@Getter
	public final int requiredPermissionLevel = 1;
	private final String[] availableCommands = { "add", "check", "empty", "fill", "restore", "set", "setmax", "zero" };

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "commands.stamina.usage";
	}

	@ParametersAreNonnullByDefault
	private void executeCheck(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;

		switch (args.length) {
		case 1:
			player = getCommandSenderAsPlayer(commandSender);
			break;
		case 2:
			player = getPlayer(server, commandSender, args[1]);
			break;
		default:
			throw new WrongUsageException("commands.stamina.check.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);

		// NOTE: ?
		notifyCommandListener(commandSender, this, "commands.stamina.check.success", player.getName(),
				stamina.getStamina(EnumStaminaType.CURRENT), stamina.getStamina(EnumStaminaType.MAXIMUM));
	}

	@ParametersAreNonnullByDefault
	private void executeZero(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;

		switch (args.length) {
		case 1:
			player = getCommandSenderAsPlayer(commandSender);
			break;
		case 2:
			player = getPlayer(server, commandSender, args[1]);
			break;
		default:
			throw new WrongUsageException("commands.stamina.zero.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		stamina.zero();
		stamina.sync(player);

		notifyCommandListener(commandSender, this, "commands.stamina.zero.success", player.getName());
	}

	@ParametersAreNonnullByDefault
	private void executeRestore(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;
		switch (args.length) {
		case 1:
			player = getCommandSenderAsPlayer(commandSender);
			break;
		case 2:
			player = getPlayer(server, commandSender, args[1]);
			break;
		default:
			throw new WrongUsageException("commands.stamina.fill.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		stamina.fill();
		stamina.sync(player);

		notifyCommandListener(commandSender, this, "commands.stamina.fill.success", player.getName());
	}

	@ParametersAreNonnullByDefault
	private void executeSet(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;
		double value;

		switch (args.length) {
		case 2:
			player = getCommandSenderAsPlayer(commandSender);
			value = parseDouble(args[1], 0);
			break;
		case 3:
			player = getPlayer(server, commandSender, args[1]);
			value = parseDouble(args[2], 0);
			break;
		default:
			throw new WrongUsageException("commands.stamina.set.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		stamina.set(EnumStaminaType.CURRENT, value);
		stamina.sync(player);

		notifyCommandListener(commandSender, this, "commands.stamina.set.success", value, player.getName());
	}

	@ParametersAreNonnullByDefault
	private void executeSetMax(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;
		double value;

		switch (args.length) {
		case 2:
			player = getCommandSenderAsPlayer(commandSender);
			value = parseDouble(args[1], 0);
			break;
		case 3:
			player = getPlayer(server, commandSender, args[1]);
			value = parseDouble(args[2], 0);
			break;
		default:
			throw new WrongUsageException("commands.stamina.setmax.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		stamina.set(EnumStaminaType.MAXIMUM, value);
		stamina.sync(player);

		notifyCommandListener(commandSender, this, "commands.stamina.setmax.success", value, player.getName());
	}

	@ParametersAreNonnullByDefault
	private void executeAdd(MinecraftServer server, ICommandSender commandSender, String[] args)
			throws CommandException {
		EntityPlayerMP player;
		double value;

		switch (args.length) {
		case 2:
			player = getCommandSenderAsPlayer(commandSender);
			value = parseDouble(args[1], 0);
			break;
		case 3:
			player = getPlayer(server, commandSender, args[1]);
			value = parseDouble(args[2], 0);
			break;
		default:
			throw new WrongUsageException("commands.stamina.add.usage");
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(player);
		stamina.add(value);
		stamina.sync(player);

		notifyCommandListener(commandSender, this, "commands.stamina.add.success", value, player.getName());
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
		if (!(commandSender instanceof EntityPlayer)) {
			return;
		}
		if (args.length < 1) {
			throw new WrongUsageException("commands.stamina.usage");
		}
		String command = args[0].toLowerCase();
		switch (command) {
		case "add":
			executeAdd(server, commandSender, args);
			break;
		case "check":
			executeCheck(server, commandSender, args);
			break;
		case "zero":
		case "empty":
			executeZero(server, commandSender, args);
			break;
		case "fill":
		case "restore":
			executeRestore(server, commandSender, args);
			break;
		case "set":
			executeSet(server, commandSender, args);
			break;
		case "setmax":
			executeSetMax(server, commandSender, args);
			break;
		default:
			throw new WrongUsageException("commands.stamina.usage");
		}
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender commandSender, String[] args,
			@Nullable BlockPos pos) {
		switch (args.length) {
		case 1:
			return Stream.of(availableCommands).filter(command -> command.startsWith(args[0]))
					.collect(Collectors.toList());
		case 2:
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		default:
			return Collections.emptyList();
		}
	}
}