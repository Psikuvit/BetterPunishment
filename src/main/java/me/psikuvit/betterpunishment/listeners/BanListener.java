package me.psikuvit.betterpunishment.listeners;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.PunishmentManager;
import me.psikuvit.betterpunishment.punishements.Ban;
import me.psikuvit.betterpunishment.punishements.Blacklist;
import me.psikuvit.betterpunishment.punishements.TempBan;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanListener implements Listener {

    private final PunishmentManager punishmentManager;

    public BanListener(BetterPunishment plugin) {
        this.punishmentManager = plugin.getPunishmentManager();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!punishmentManager.isBanned(event.getPlayer().getUniqueId())) {
            return;
        }
        Punishment punishment = punishmentManager.getPlayerPunishment(event.getPlayer().getUniqueId(), PunishmentTypes.TEMP_BAN, PunishmentTypes.BAN);

        if (punishment instanceof TempBan) {
            TempBan tempBan = (TempBan) punishment;

            OfflinePlayer banner = Bukkit.getOfflinePlayer(tempBan.getPunisher());

            if (System.currentTimeMillis() < tempBan.getEndTime()) {
                long remainingTime = (tempBan.getEndTime() - System.currentTimeMillis()) / 1000;
                long unbanDate = tempBan.getEndTime() / 1000;

                event.setKickMessage(Utils.color(
                        "&cYou have been banned from the server by " + banner.getName() + " for " + Utils.getDurationString(remainingTime)
                                + ". Your ban will expire: " + Utils.getDate(unbanDate) + ". Reason: " + punishment.getReason()));
                event.setResult(PlayerLoginEvent.Result.KICK_BANNED);

            } else {
                punishmentManager.unPunish(tempBan.getPunished(), PunishmentTypes.TEMP_BAN);
            }
        } else if (punishment instanceof Ban) {
            Ban ban = (Ban) punishment;
            OfflinePlayer banner = Bukkit.getOfflinePlayer(ban.getPunisher());

            event.setKickMessage(Utils.color(
                    "&cYou have been banned from the server by " + banner.getName() + " forever. Reason: " + punishment.getReason()));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);

        } else if (punishment instanceof Blacklist) {
            Blacklist blacklist = (Blacklist) punishment;
            OfflinePlayer blacklister = Bukkit.getOfflinePlayer(blacklist.getPunisher());

            Bukkit.getBanList(BanList.Type.IP).addBan(event.getAddress().getHostAddress(), "Banned by plugin", null, "Console");
            event.setKickMessage(Utils.color(
                    "&cYou have been blacklisted from the server by " + blacklister.getName() + " forever. Reason: " + punishment.getReason()));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
    }
}
