package me.psikuvit.betterpunishment.commands.args;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.commands.CommandAbstract;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MuteArg extends CommandAbstract {

    public MuteArg(BetterPunishment plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player banner = (Player) sender;
        if (!banner.isOp()) {
            banner.sendMessage(Utils.color("&cNo perm!"));
            return;
        }
        OfflinePlayer toMute = Bukkit.getOfflinePlayer(args[0]);

        if (!toMute.hasPlayedBefore()) {
            sender.sendMessage(Utils.color("&cPlayer not found."));
            return;
        }
        if (plugin.getPunishmentManager().isMuted(toMute.getUniqueId())) {
            banner.sendMessage(Utils.color("&cPlayer is already muted!"));
            return;
        }

        String reason = args[1];

        plugin.getPunishmentManager().punish(banner.getUniqueId(), toMute.getUniqueId(), PunishmentTypes.MUTE, reason, 0, Utils.getNowDate());

        banner.sendMessage(Utils.color("&b" + toMute.getName() + " was muted. Reason: " + reason));

    }

    @Override
    public String correctArg() {
        return "/bp mute <player> <reason>";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public int requiredArg() {
        return 2;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return Collections.emptyList();
    }
}