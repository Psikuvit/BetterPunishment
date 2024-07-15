package me.psikuvit.betterpunishment.commands;

import me.psikuvit.betterpunishment.BetterPunishment;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CommandAbstract {

    protected BetterPunishment plugin;

    public CommandAbstract(final BetterPunishment plugin) {
        this.plugin = plugin;
    }

    public abstract void executeCommand(final String[] args, final CommandSender sender);

    public abstract String correctArg();

    public abstract boolean onlyPlayer();

    public abstract int requiredArg();

    public abstract List<String> tabComplete(final String[] args);
}