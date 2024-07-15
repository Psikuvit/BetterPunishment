package me.psikuvit.betterpunishment;

import me.psikuvit.betterpunishment.punishements.Ban;
import me.psikuvit.betterpunishment.punishements.Blacklist;
import me.psikuvit.betterpunishment.punishements.Kick;
import me.psikuvit.betterpunishment.punishements.Mute;
import me.psikuvit.betterpunishment.punishements.TempBan;
import me.psikuvit.betterpunishment.punishements.TempMute;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class PunishmentManager {

    private final List<Punishment> activePunishments;
    private final List<Punishment> nonActive;

    public PunishmentManager() {
        activePunishments = new ArrayList<>();
        nonActive = new ArrayList<>();
    }

    public boolean isBanned(UUID uuid) {
        for (Punishment punishment : activePunishments) {
            if (punishment.getPunished().equals(uuid) &&
                    (punishment.getPunishmentType() == PunishmentTypes.BAN ||
                            punishment.getPunishmentType() == PunishmentTypes.TEMP_BAN)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMuted(UUID uuid) {
        for (Punishment punishment : activePunishments) {
            if (punishment.getPunished().equals(uuid) &&
                    (punishment.getPunishmentType() == PunishmentTypes.MUTE ||
                            punishment.getPunishmentType() == PunishmentTypes.TEMP_MUTE)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlacklisted(UUID uuid) {
        for (Punishment punishment : activePunishments) {
            if (punishment.getPunished().equals(uuid) &&
                    (punishment.getPunishmentType() == PunishmentTypes.BLACKLIST)) {
                return true;
            }
        }
        return false;
    }

    public Punishment getPlayerPunishment(UUID uuid, PunishmentTypes... punishmentType) {
        for (Punishment punishment : activePunishments) {
            if (punishment.getPunished().equals(uuid) && Arrays.asList(punishmentType).contains(punishment.getPunishmentType())) return punishment;
        }
        return null;
    }

    public boolean unPunish(UUID uuid, PunishmentTypes punishmentType) {
        nonActive.add(getPlayerPunishment(uuid, punishmentType));
        return activePunishments.remove(getPlayerPunishment(uuid, punishmentType));
    }

    public void punish(UUID punisher, UUID punished, PunishmentTypes punishmentTypes, String reason, long duration, String date) {
        Punishment punishment;

        if (!punishmentTypes.isPermanent()) {
            switch (punishmentTypes) {
                case TEMP_BAN:
                    punishment = new TempBan(punisher, punished, reason, duration, date);
                    break;
                case TEMP_MUTE:
                    punishment = new TempMute(punisher, punished, reason, duration, date);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported punishment type: " + punishmentTypes);
            }
        } else {
            switch (punishmentTypes) {
                case BAN:
                    punishment = new Ban(punisher, punished, reason, date);
                    break;
                case KICK:
                    punishment = new Kick(punisher, punished, reason, date);
                    break;
                case MUTE:
                    punishment = new Mute(punisher, punished, reason, date);
                    break;
                case BLACKLIST:
                    punishment = new Blacklist(punisher, punished, reason, date);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported punishment type: " + punishmentTypes);
            }
        }

        punishment.execute();
        activePunishments.add(punishment);
    }

    public List<Punishment> getActivePunishments() {
        return activePunishments;
    }

    public List<Punishment> getNonActive() {
        return nonActive;
    }
}
