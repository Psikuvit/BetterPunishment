package me.psikuvit.betterpunishment.commands.args;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.commands.CommandAbstract;
import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryArg extends CommandAbstract {

    public HistoryArg(BetterPunishment plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player player = (Player) sender;

        player.sendMessage(Utils.color("&bPunishments History:"));
        if (args[0].equalsIgnoreCase("active")) {
            List<Punishment> punishments = new ArrayList<>(plugin.getPunishmentManager().getActivePunishments());

            for (Punishment punishment : punishments) {
                OfflinePlayer punisher = Bukkit.getOfflinePlayer(punishment.getPunisher());
                OfflinePlayer punished = Bukkit.getOfflinePlayer(punishment.getPunished());
                String type = punishment.getPunishmentType().name();
                String reason = punishment.getReason();
                String date = punishment.getDate();

                if (punishment instanceof Durable) {
                    Durable durable = (Durable) punishment;

                    String duration = Utils.getDurationString(durable.getDuration());

                    long remainingTime = (durable.getEndTime() - System.currentTimeMillis()) / 1000;
                    String remainingTimeString = Utils.getDurationString(remainingTime);

                    player.sendMessage(Utils.color("&b- " + type + ": by " + punisher.getName() + " punished: " + punished.getName() + " for " + reason + " on " + date
                            + " for " + duration + ", Remaining time: " + remainingTimeString + ", End on: " + Utils.getDate(durable.getEndTime())));

                    return;
                }
                player.sendMessage(Utils.color("&b- " + type + ": by " + punisher.getName() + " punished: " + punished.getName() + " for " + reason + " on " + date));
            }

        } else if (args[0].equalsIgnoreCase("nonactive")) {
            List<Punishment> punishments = new ArrayList<>(plugin.getPunishmentManager().getNonActive());

            for (Punishment punishment : punishments) {
                OfflinePlayer punisher = Bukkit.getOfflinePlayer(punishment.getPunisher());
                OfflinePlayer punished = Bukkit.getOfflinePlayer(punishment.getPunished());
                String type = punishment.getPunishmentType().name();
                String reason = punishment.getReason();
                String date = punishment.getDate();

                if (punishment instanceof Durable) {
                    Durable durable = (Durable) punishment;

                    String duration = Utils.getDurationString(durable.getDuration());

                    long remainingTime = (durable.getEndTime() - System.currentTimeMillis()) / 1000;
                    String remainingTimeString = Utils.getDurationString(remainingTime);

                    player.sendMessage(Utils.color("&b- " + type + ": by " + punisher.getName() + " punished: " + punished.getName() + " for " + reason + " on " + date
                            + " for " + duration + ", Remaining time: " + remainingTimeString + ", End on: " + Utils.getDate(durable.getEndTime())));

                    return;
                }
                player.sendMessage(Utils.color("&b- " + type + ": by " + punisher.getName() + " punished: " + punished.getName() + " for " + reason + " on " + date));
            }
        }
    }

    @Override
    public String correctArg() {
        return "/bp history <active/nonactive>";
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
