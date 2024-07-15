package me.psikuvit.betterpunishment.punishements;

import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;

import java.util.UUID;

public class Mute implements Punishment {

    private final UUID punisher;
    private final UUID punished;
    private final String reason;
    private final String date;

    public Mute(UUID punisher, UUID punished, String reason, String date) {
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
        return PunishmentTypes.MUTE;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void execute() {}
}