package net.braunly.ponymagic.command;

import java.lang.invoke.WrongMethodTypeException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import lombok.Getter;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.event.LevelUpEvent;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

public class CommandMagic extends CommandBase {
	@Getter
	public final List<String> aliases = Lists.newArrayList("magic");
	@Getter
	public final String name = "magic";
	@Getter
	public final int requiredPermissionLevel = 1;
	private final String[] availableCommands = { "race", "spell",

			"test" };

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
		PlayerData playerData = PlayerDataController.instance.getDataFromUsername(PonyMagic.Server, playerName);
		// Set new race
		playerData.race = race;
		
		// Clean all stored data
		playerData.clean();
		playerData.save();
		
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

		PlayerData playerData = PlayerDataController.instance.getDataFromUsername(PonyMagic.Server, playerName);

		if (playerData.race.hasSpell(spellName)) {
			playerData.skillData.upLevel(spellName);
			playerData.save();
		} else {
			player.sendMessage(new TextComponentTranslation("commands.magic.spell.notAvailable", playerName));
		}
	}

	@ParametersAreNonnullByDefault
	private void executeTest(EntityPlayerMP player) throws CommandException {
		PlayerData playerData = PlayerDataController.instance.getDataFromUsername(PonyMagic.Server, player.getName());

		playerData.race = EnumRace.UNICORN;
		playerData.levelData.addExp(10000D);
		playerData.levelData.addLevel(30);
		playerData.levelData.addFreeSkillPoints(10);
		MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, playerData.levelData.getLevel()));
		playerData.save();		
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
			executeTest(player);
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
		default:
			return Collections.emptyList();
		}
	}
}