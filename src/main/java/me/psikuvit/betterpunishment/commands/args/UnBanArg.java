package me.psikuvit.betterpunishment.commands.args;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.commands.CommandAbstract;
import me.psikuvit.betterpunishment.punishements.Ban;
import me.psikuvit.betterpunishment.punishements.TempBan;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class UnBanArg extends CommandAbstract {

    public UnBanArg(BetterPunishment plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player unbanner = (Player) sender;
        if (!unbanner.isOp()) {
            unbanner.sendMessage(Utils.color("&cNo perm!"));
            return;
        }
        OfflinePlayer toUnBan = Bukkit.getOfflinePlayer(args[0]);

        if (!toUnBan.hasPlayedBefore()) {
            sender.sendMessage(Utils.color("&cPlayer not found."));
            return;
        }
        if (!plugin.getPunishmentManager().isBanned(toUnBan.getUniqueId())) {
            unbanner.sendMessage(Utils.color("&cPlayer is not banned!"));
            return;
        }

        Punishment punishment = plugin.getPunishmentManager().getPlayerPunishment(toUnBan.getUniqueId(), PunishmentTypes.BAN, PunishmentTypes.TEMP_BAN);
        PunishmentTypes punishmentType = null;
        if (punishment instanceof TempBan) {
            punishmentType = PunishmentTypes.TEMP_BAN;
        } else if (punishment instanceof Ban) {
            punishmentType = PunishmentTypes.BAN;
        }

        if (plugin.getPunishmentManager().unPunish(toUnBan.getUniqueId(), punishmentType)) {
            unbanner.sendMessage(Utils.color("&b" + toUnBan.getName() + " was unbanned."));
        }
    }

    @Override
    public String correctArg() {
        return "/bp unban <player>";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public int requiredArg() {
        return 1;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return Collections.emptyList();
    }
}
