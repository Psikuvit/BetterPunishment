package me.psikuvit.betterpunishment.punishements;

import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Blacklist implements Punishment {

    private final UUID punisher;
    private final UUID punished;
    private final String reason;
    private final String date;

    public Blacklist(UUID punisher, UUID punished, String reason, String date) {
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
        this.date = date;
    }

    @Override
    public UUID getPunisher() {
        return punisher;
    }

    @Override
    public UUID getPunished() {
        return punished;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public PunishmentTypes getPunishmentType() {
        return PunishmentTypes.BLACKLIST;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void execute() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(punished);
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            Player banner = Bukkit.getPlayer(punisher);
            player.kickPlayer(Utils.color("&cYou have been banned from the server by " + banner.getName() + " forever"));
        }
    }
}
