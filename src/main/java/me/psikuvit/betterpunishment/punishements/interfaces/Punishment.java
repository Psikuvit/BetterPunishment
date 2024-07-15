package me.psikuvit.betterpunishment.punishements.interfaces;

import me.psikuvit.betterpunishment.utils.PunishmentTypes;

import java.util.UUID;

public interface Punishment {

    UUID getPunisher();
    UUID getPunished();
    String getReason();
    PunishmentTypes getPunishmentType();
    String getDate();
    void execute();
}
