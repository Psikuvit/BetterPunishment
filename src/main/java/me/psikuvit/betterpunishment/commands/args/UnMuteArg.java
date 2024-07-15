package me.psikuvit.betterpunishment.commands.args;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.commands.CommandAbstract;
import me.psikuvit.betterpunishment.punishements.Mute;
import me.psikuvit.betterpunishment.punishements.TempMute;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class UnMuteArg extends CommandAbstract {

    public UnMuteArg(BetterPunishment plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player unMuter = (Player) sender;
        if (!unMuter.isOp()) {
            unMuter.sendMessage(Utils.color("&cNo perm!"));
            return;
        }
        OfflinePlayer toUnMute = Bukkit.getOfflinePlayer(args[0]);

        if (!toUnMute.hasPlayedBefore()) {
            sender.sendMessage(Utils.color("&cPlayer not found."));
            return;
        }
        if (!plugin.getPunishmentManager().isMuted(toUnMute.getUniqueId())) {
            unMuter.sendMessage(Utils.color("&cPlayer is not muted!"));
            return;
        }

        Punishment punishment = plugin.getPunishmentManager().getPlayerPunishment(toUnMute.getUniqueId(), PunishmentTypes.MUTE, PunishmentTypes.TEMP_MUTE);
        PunishmentTypes punishmentType = null;
        if (punishment instanceof TempMute) {
            punishmentType = PunishmentTypes.TEMP_MUTE;
        } else if (punishment instanceof Mute) {
            punishmentType = PunishmentTypes.MUTE;
        }

        if (plugin.getPunishmentManager().unPunish(toUnMute.getUniqueId(), punishmentType)) {
            unMuter.sendMessage(Utils.color("&b" + toUnMute.getName() + " was unmuted."));
        }
    }

    @Override
    public String correctArg() {
        return "/bp unmute <player>";
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
