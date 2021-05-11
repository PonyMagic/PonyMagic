package net.braunly.ponymagic.command;

import com.google.common.collect.Lists;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumRace;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.skill.Skill;
import net.braunly.ponymagic.spells.Spell;
import net.braunly.ponymagic.spells.SpellStorage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommandCast extends CommandBase {
	public static final String NAME = "cast";
	public static final int REQUIRED_PERMISSION_LEVEL = 0;
	public static final List<String> aliases = Lists.newArrayList(NAME);

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
		return "commands.cast.usage";
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
		if (!(commandSender instanceof EntityPlayer)) {
			return;
		}
		if (args.length < 1) {
			throw new WrongUsageException("commands.cast.usage");
		}
		EntityPlayerMP player = (EntityPlayerMP) commandSender;
		String spellName = args[0].toLowerCase();

		// Fail-fast; all players should have at least REGULAR race
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		EnumRace playerRace = Optional.ofNullable(playerData.getRace())
				.orElseThrow(() -> new IllegalStateException("Got null race for player " + player.getName()));

		if (playerRace == EnumRace.REGULAR) {
			player.sendMessage(new TextComponentTranslation("commands.cast.withoutRace", ""));
			return;
		}

		if (!playerRace.hasSpell(spellName)) {
			player.sendMessage(new TextComponentTranslation("commands.cast.haveNoAccess", ""));
			return;
		}

		if (!playerData.getSkillData().isSkillLearned(spellName)) {
			player.sendMessage(new TextComponentTranslation("commands.cast.notLearned", ""));
			return;
		}

		int spellLevel = playerData.getSkillData().getSkillLevel(spellName);
		Spell spell = SpellStorage.getInstance().getSpell(spellName);
		Skill skillConfig = SkillConfig.getRaceSkill(playerRace, spellName, spellLevel);

		boolean result;
		if (args.length >= 2) {
			result = spell.cast(player, skillConfig, args);
		} else {
			result = spell.cast(player, skillConfig);
		}

		if (result) {
			player.sendMessage(new TextComponentTranslation("commands.cast.ok", ""));
		} else {
			player.sendMessage(new TextComponentTranslation("commands.cast.fail", ""));
		}
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return Collections.emptyList();
	}
}