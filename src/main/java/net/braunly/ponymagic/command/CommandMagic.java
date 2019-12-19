package net.braunly.ponymagic.command;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.events.LevelUpEvent;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.invoke.WrongMethodTypeException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandMagic extends CommandBase {
	@Getter
	public final List<String> aliases = Lists.newArrayList("magic");
	@Getter
	public final String name = "magic";
	@Getter
	public final int requiredPermissionLevel = 1;
	private final String[] availableCommands = { "race", "spell", "test" };

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "commands.magic.usage";
	}

	@ParametersAreNonnullByDefault
	private void executeRace(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.race.usage");
		}
		String playerName = args[1];
		String raceName = args[2];

		EnumRace race = EnumRace.getByName(raceName)
				.orElseThrow(() -> new WrongMethodTypeException("commands.magic.race.usage"));
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);
		// Set new race
		playerData.setRace(race);
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
		
		// Send changes to client
		PonyMagic.channel.sendTo(new PlayerDataPacket(playerData.getNBT()), player);
		player.sendMessage(
				new TextComponentTranslation("commands.magic.race.success", playerName, race.getLocalizedName()));
	}

	@ParametersAreNonnullByDefault
	private void executeSpell(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.spell.usage");
		}
		String playerName = args[1];
		String spellName = args[2];

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);

		if (playerData.getRace().hasSpell(spellName)) {
			playerData.getSkillData().upSkillLevel(spellName);
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
		} else {
			player.sendMessage(new TextComponentTranslation("commands.magic.spell.notAvailable", playerName));
		}
	}

	@ParametersAreNonnullByDefault
	private void executeTest(EntityPlayerMP player, String[] args) throws CommandException {
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player.getName());

		String raceName = args[1];
		EnumRace race = EnumRace.getByName(raceName)
				.orElseThrow(() -> new WrongMethodTypeException("commands.magic.race.usage"));

		playerData.setRace(race);
		playerData.getLevelData().addExp(10000D);
		playerData.getLevelData().setLevel(30);
		playerData.getLevelData().setFreeSkillPoints(10);
		MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, playerData.getLevelData().getLevel()));
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
		if (!(commandSender instanceof EntityPlayer)) {
			return;
		}
		if (args.length < 1) {
			throw new WrongUsageException("commands.magic.usage");
		}
		String command = args[0].toLowerCase();

		EntityPlayerMP player = Optional.ofNullable((EntityPlayerMP) commandSender.getCommandSenderEntity())
				// FIXME: ...or just return?
				.orElseThrow(() -> new WrongUsageException("Не найден игрок!"));

		switch (command) {
		case "race":
			executeRace(player, args);
			break;
		case "spell":
			executeSpell(player, args);
			break;
		case "test":
			executeTest(player, args);
			break;
		default:
			throw new WrongUsageException("commands.magic.usage");
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
				return Stream.of(server.getOnlinePlayerNames()).filter(playerName -> playerName.startsWith(args[1]))
						.collect(Collectors.toList());
			default:
				return Collections.emptyList();
		}
	}
}