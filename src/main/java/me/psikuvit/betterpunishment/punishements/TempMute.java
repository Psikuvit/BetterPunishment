package me.psikuvit.betterpunishment.punishements;

import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;

import java.util.UUID;

public class TempMute implements Punishment, Durable {

    private final UUID punisher;
    private final UUID punished;
    private final String reason;
    private final String date;
    private long duration;
    private final long endTime;

    public TempMute(UUID punisher, UUID punished, String reason, long duration, String date) {
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
        this.duration = duration;
        this.date = date;
        this.endTime = System.currentTimeMillis() + (duration * 1000L);
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
        return PunishmentTypes.TEMP_MUTE;
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
        return 0;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public void execute() {}
}
