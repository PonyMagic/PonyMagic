package net.braunly.ponymagic.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import lombok.Getter;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.race.EnumRace;
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

public class CommandCast extends CommandBase {
	@Getter
	public final List<String> aliases = Lists.newArrayList("cast");
	@Getter
	public final String name = "cast";
	@Getter
	public final int requiredPermissionLevel = 0;

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
		// FIXME: (orhideous) make getPlayerData contract more strict
		PlayerData playerData = PlayerDataController.instance.getPlayerData(player);
		EnumRace playerRace = Optional.ofNullable(playerData.race)
				.orElseThrow(() -> new IllegalStateException("Got null race for player " + player.getName()));

		if (playerRace == EnumRace.REGULAR) {
			player.sendMessage(new TextComponentTranslation("commands.cast.withoutRace", ""));
			return;
		}

		if (!playerRace.hasSpell(spellName)) {
			player.sendMessage(new TextComponentTranslation("commands.cast.haveNoAccess", ""));
			return;
		}

		if (!playerData.skillData.isSkillLearned(spellName)) {
			player.sendMessage(new TextComponentTranslation("commands.cast.notLearned", ""));
			return;
		}

		int spellLevel = playerData.skillData.getSkillLevel(spellName);
		Spell spell = SpellStorage.getInstance().getSpell(spellName);

		if (spell.cast(player, spellLevel)) {
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