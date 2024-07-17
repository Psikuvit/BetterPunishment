package me.psikuvit.betterpunishment.database;

import me.psikuvit.betterpunishment.punishements.Ban;
import me.psikuvit.betterpunishment.punishements.Blacklist;
import me.psikuvit.betterpunishment.punishements.Kick;
import me.psikuvit.betterpunishment.punishements.Mute;
import me.psikuvit.betterpunishment.punishements.TempBan;
import me.psikuvit.betterpunishment.punishements.TempMute;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;

import java.util.UUID;

public interface Database {

    void loadData();
    void saveData();

    default Punishment initPunishment(UUID punisher, UUID punished, String reason, PunishmentTypes type, long remaining, String date) {
        Punishment punishment;

        if (!type.isPermanent()) {
            switch (type) {
                case TEMP_BAN:
                    punishment = new TempBan(punisher, punished, reason, remaining, date);
                    break;
                case TEMP_MUTE:
                    punishment = new TempMute(punisher, punished, reason, remaining, date);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported punishment type: " + type);
            }
        } else {
            switch (type) {
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
                    throw new IllegalArgumentException("Unsupported punishment type: " + type);
            }
        }
        return punishment;
    }
}
