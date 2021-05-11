package net.braunly.ponymagic.command;

import com.google.common.collect.Lists;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.events.LevelUpEvent;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.LevelConfig;
import net.braunly.ponymagic.config.PortalConfig;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.entity.EntityPortal;
import net.braunly.ponymagic.handlers.MagicHandlersContainer;
import net.braunly.ponymagic.network.packets.PlayerDataPacket;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandMagic extends CommandBase {
	public static final String NAME = "magic";
	public static final int REQUIRED_PERMISSION_LEVEL = 1;
	public static final List<String> aliases = Lists.newArrayList(NAME);
	private final String[] availableCommands = {
			"race", "reload", "spell", "test",
			"setlevel", "setpoints", "getquest",
			"setportal", "deleteportal", "listportal"
	};

	@Override
	@Nonnull
	public String getName() {
		return NAME;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return REQUIRED_PERMISSION_LEVEL;
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "commands.magic.usage";
	}


	@ParametersAreNonnullByDefault
	private void executeTest(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.test.usage");
		}
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player.getName());

		String raceName = args[1];
		EnumRace race = EnumRace.getByName(raceName)
				.orElseThrow(() -> new WrongUsageException("commands.magic.race.not_found"));

		int level = PonyMagic.MAX_LVL;
		playerData.setRace(EnumRace.REGULAR);
		playerData.setRace(race);
		playerData.getLevelData().setLevel(level);
		playerData.getLevelData().setFreeSkillPoints(level);
		MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, level));
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
		MagicHandlersContainer.updatePlayerFlySpeed(playerData, 0.0F);
		MagicHandlersContainer.updatePlayerMaxStamina(playerData);

		player.world.spawnEntity(new EntityPortal(player.world, player.posX, player.posY, player.posZ, new BlockPos(-1786,70,-4615), "Портал в Нарнію"));

		// Send changes to client
		PonyMagic.channel.sendTo(new PlayerDataPacket(playerData.getNBT()), player);
	}

	@ParametersAreNonnullByDefault
	private void executeRace(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.race.usage");
		}
		String playerName = args[1];
		String raceName = args[2];

		EnumRace race = EnumRace.getByName(raceName)
				.orElseThrow(() -> new WrongUsageException("commands.magic.race.not_found"));
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);
		// Set new race
		playerData.setRace(race);
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
		MagicHandlersContainer.updatePlayerFlySpeed(playerData, 0.0F);
		MagicHandlersContainer.updatePlayerMaxStamina(playerData);
		MagicHandlersContainer.disablePlayerFlyAbility(player);
		
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
			player.sendMessage(new TextComponentString(String.format("%s now has %s spell",
					playerName, spellName)));
		} else {
			player.sendMessage(new TextComponentTranslation("commands.magic.spell.notAvailable", playerName));
		}
	}


	@ParametersAreNonnullByDefault
	private void executeSetLevel(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.setlevel.usage");
		}
		String playerName = args[1];
		int level = MathHelper.getInt(args[2], 0);

		if (level > PonyMagic.MAX_LVL) {
			throw new WrongUsageException("commands.magic.setlevel.max_level");
		}

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);
		playerData.getLevelData().setLevel(level);
		playerData.getLevelData().setGoals(LevelConfig.getRaceLevelConfig(playerData.getRace(), level+1).getQuestsWithGoals());
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
		player.sendMessage(new TextComponentString(String.format("%s now has %s level",
				playerName, playerData.getLevelData().getLevel())));
	}

	@ParametersAreNonnullByDefault
	private void executeSetPoints(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.setpoints.usage");
		}
		String playerName = args[1];
		int points = MathHelper.getInt(args[2], 0);

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);
		playerData.getLevelData().setFreeSkillPoints(points);
		PonyMagicAPI.playerDataController.savePlayerData(playerData);
		player.sendMessage(new TextComponentString(String.format("%s now has %s free skill points",
				playerName, playerData.getLevelData().getFreeSkillPoints())));
	}

	@ParametersAreNonnullByDefault
	private void executeReload(EntityPlayerMP player) {
		LevelConfig.load();
		SkillConfig.load();
		PortalConfig.load();
		player.sendMessage(new TextComponentString("Configs reloaded!"));
	}

	@ParametersAreNonnullByDefault
	private void executeGetQuest(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.getquest.usage");
		}
		String playerName = args[1];

		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(playerName);
		for (Map.Entry<String, HashMap<String, Integer>> questEntry : playerData.getLevelData().getCurrentGoals().entrySet()) {
			String questName = new TextComponentTranslation("quest." + questEntry.getKey() + ".name").getUnformattedComponentText();
			player.sendMessage(new TextComponentTranslation("commands.magic.getquest.quest", questName));
			for (Map.Entry<String, Integer> goalEntry : questEntry.getValue().entrySet()) {
				player.sendMessage(new TextComponentTranslation(
						"commands.magic.getquest.goal",
						QuestGoalUtils.getLocalizedGoalName(goalEntry.getKey()),
						goalEntry.getValue()
				));
			}
		}
	}

	@ParametersAreNonnullByDefault
	private void executeSetPortal(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.setportal.usage");
		}

		String portalName = args[1];
		BlockPos portalPos = player.getPosition();
		if (args.length == 5) {
			portalPos = parseBlockPos(player, args, 2, true);
		}

		PortalConfig.addPortal(portalName, portalPos);
		player.sendMessage(new TextComponentString(String.format("Portal %s created!", portalName)));
	}

	@ParametersAreNonnullByDefault
	private void executeDeletePortal(EntityPlayerMP player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.magic.deleteportal.usage");
		}

		String portalName = args[1];

		PortalConfig.deletePortal(portalName);
		player.sendMessage(new TextComponentString(String.format("Portal %s deleted!", portalName)));
	}

	@ParametersAreNonnullByDefault
	private void executeListPortal(EntityPlayerMP player, String[] args) {
		Map<String, BlockPos> portalsMap = PortalConfig.getPortalsMap();

		for (Map.Entry<String, BlockPos> portalEntry : portalsMap.entrySet()) {
			player.sendMessage(new TextComponentTranslation(
					"commands.magic.listportal.portal",
					portalEntry.getKey(),
					portalEntry.getValue().getX(),
					portalEntry.getValue().getY(),
					portalEntry.getValue().getZ()
			));
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
		if (!(commandSender instanceof EntityPlayer)) {
			return;
		}
		if (args.length < 1) {
			throw new WrongUsageException(getUsage(commandSender));
		}
		String command = args[0].toLowerCase();

		EntityPlayerMP player = Optional.ofNullable((EntityPlayerMP) commandSender.getCommandSenderEntity())
				.orElseThrow(() -> new WrongUsageException("commands.magic.player_not_found"));

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
			case "setpoints":
				executeSetPoints(player, args);
				break;
			case "setlevel":
				executeSetLevel(player, args);
				break;
			case "reload":
				executeReload(player);
				break;
			case "getquest":
				executeGetQuest(player, args);
				break;
			case "setportal":
				executeSetPortal(player, args);
				break;
			case "deleteportal":
				executeDeletePortal(player, args);
				break;
			case "listportal":
				executeListPortal(player, args);
				break;
			default:
				throw new WrongUsageException(getUsage(commandSender));
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