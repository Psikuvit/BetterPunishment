package me.psikuvit.betterpunishment.commands.args;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.commands.CommandAbstract;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KickArg extends CommandAbstract {

    public KickArg(BetterPunishment plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player kicker = (Player) sender;

        Player toKick = Bukkit.getPlayer(args[0]);
        if (toKick == null) {
            kicker.sendMessage(Utils.color("&cCouldn't find player"));
            return;
        }

        plugin.getPunishmentManager().punish(kicker.getUniqueId(), toKick.getUniqueId(), PunishmentTypes.KICK, args[1], 0, Utils.getNowDate());
        plugin.getPunishmentManager().unPunish(toKick.getUniqueId(), PunishmentTypes.KICK);

        kicker.sendMessage(Utils.color("&bKicked " + toKick.getName() + " for " + args[1]));
    }

    @Override
    public String correctArg() {
        return "/bp kick <player> <reason>";
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
