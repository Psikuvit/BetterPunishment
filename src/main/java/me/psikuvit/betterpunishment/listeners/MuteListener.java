package me.psikuvit.betterpunishment.listeners;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.PunishmentManager;
import me.psikuvit.betterpunishment.punishements.Mute;
import me.psikuvit.betterpunishment.punishements.TempMute;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener {

    private final PunishmentManager punishmentManager;

    public MuteListener(BetterPunishment plugin) {
        this.punishmentManager = plugin.getPunishmentManager();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player punished = event.getPlayer();
        if (!punishmentManager.isMuted(punished.getUniqueId())) {
            return;
        }

        Punishment punishment = punishmentManager.getPlayerPunishment(punished.getUniqueId(), PunishmentTypes.MUTE, PunishmentTypes.TEMP_MUTE);
        if (punishment instanceof TempMute) {
            TempMute tempMute = (TempMute) punishment;

            OfflinePlayer muter = Bukkit.getOfflinePlayer(tempMute.getPunisher());

            if (System.currentTimeMillis() < tempMute.getEndTime()) {
                long remainingTime = (tempMute.getEndTime() - System.currentTimeMillis()) / 1000;
                long unbanDate = tempMute.getEndTime() / 1000;

                punished.sendMessage(Utils.color(
                        "&cYou have been muted by " + muter.getName() + " for " + Utils.getDurationString(remainingTime)
                                + ". Your mute will expire: " + Utils.getDate(unbanDate) + ". Reason: " + punishment.getReason()));
                event.setCancelled(true);
            } else {
                punishmentManager.unPunish(tempMute.getPunished(), PunishmentTypes.TEMP_MUTE);
            }

        } else if (punishment instanceof Mute) {
            Mute ban = (Mute) punishment;
            OfflinePlayer muter = Bukkit.getOfflinePlayer(ban.getPunisher());

            punished.sendMessage(Utils.color(
                    "&cYou have been muted by " + muter.getName() + " forever. Reason: " + punishment.getReason()));
            event.setCancelled(true);

        }
    }
}
