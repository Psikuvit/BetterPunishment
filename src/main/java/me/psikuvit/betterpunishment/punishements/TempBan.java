package me.psikuvit.betterpunishment.punishements;

import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempBan implements Punishment, Durable {

    private final UUID punisher;
    private final UUID punished;
    private final String reason;
    private final String date;
    private long duration;
    private final long endTime;
    protected final long remaining;

    public TempBan(UUID punisher, UUID punished, String reason, long remaining, String date) {
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
        this.duration = remaining;
        this.endTime = System.currentTimeMillis() + (duration * 1000L);
        this.date = date;
        this.remaining = remaining;
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
        return PunishmentTypes.TEMP_BAN;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getRemaining() {
        return remaining;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public void execute() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(punished);
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            Player banner = Bukkit.getPlayer(punisher);
            player.kickPlayer(Utils.color("&cYou have been banned from the server by " + banner.getName() + " for " + Utils.getDurationString(duration)));
        }
    }
}
