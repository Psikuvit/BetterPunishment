package me.psikuvit.betterpunishment.punishements;

import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Kick implements Punishment {

    private final UUID punisher;
    private final UUID punished;
    private final String reason;
    private final String date;

    public Kick(UUID punisher, UUID punished, String reason, String date) {
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
        return PunishmentTypes.KICK;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void execute() {
        Player player = Bukkit.getPlayer(punished);
        if (player != null && player.isOnline()) {
            Player banner = Bukkit.getPlayer(punisher);
            player.kickPlayer(Utils.color("&cYou have been kicked from the server by " + banner.getName()));
        }
    }
}
