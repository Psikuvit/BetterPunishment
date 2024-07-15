package me.psikuvit.betterpunishment.database;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.PunishmentManager;
import me.psikuvit.betterpunishment.punishements.Ban;
import me.psikuvit.betterpunishment.punishements.Blacklist;
import me.psikuvit.betterpunishment.punishements.Kick;
import me.psikuvit.betterpunishment.punishements.Mute;
import me.psikuvit.betterpunishment.punishements.TempBan;
import me.psikuvit.betterpunishment.punishements.TempMute;
import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLData {

    private final MySQL mySQL;
    private final PunishmentManager punishmentManager;

    public MySQLData(BetterPunishment plugin) {
        this.mySQL = plugin.getMySQL();
        this.punishmentManager = plugin.getPunishmentManager();
    }

    public void loadTeams() {
        try (Connection connection = mySQL.getConnection()) {

            PreparedStatement activeStmt = connection.prepareStatement("SELECT punisher, punished, reason, type, duration, remaining_duration, ban_time FROM active_punishments");
            ResultSet activeSet = activeStmt.executeQuery();

            while (activeSet.next()) {
                UUID punisher = UUID.fromString(activeSet.getString("punisher"));
                UUID punished = UUID.fromString(activeSet.getString("punished"));

                String reason = activeSet.getString("reason");

                String typeString = activeSet.getString("type");
                PunishmentTypes type = PunishmentTypes.valueOf(typeString);

                String durationString = activeSet.getString("duration");
                long duration = Utils.parse(durationString);

                String remainingDurationString = activeSet.getString("remaining_duration");
                long remainingDuration = Utils.parse(remainingDurationString);

                String banTime = activeSet.getString("ban_time");

                Punishment punishment = initPunishment(punisher, punished, reason, type, remainingDuration, banTime);
                if (punishment instanceof Durable) {
                    ((Durable) punishment).setDuration(duration);
                }

                punishmentManager.getActivePunishments().add(punishment);

            }

            PreparedStatement unactiveStmt = connection.prepareStatement("SELECT punisher, punished, reason, type, duration, ban_time FROM unactive_punishments");
            ResultSet unactiveSet = unactiveStmt.executeQuery();

            while (unactiveSet.next()) {
                UUID punisher = UUID.fromString(unactiveSet.getString("punisher"));
                UUID punished = UUID.fromString(unactiveSet.getString("punished"));

                String reason = unactiveSet.getString("reason");

                String typeString = unactiveSet.getString("type");
                PunishmentTypes type = PunishmentTypes.valueOf(typeString);

                String durationString = unactiveSet.getString("duration");
                long duration = Utils.parse(durationString);

                String banTime = unactiveSet.getString("ban_time");

                Punishment punishment = initPunishment(punisher, punished, reason, type, duration, banTime);
                if (punishment instanceof Durable) {
                    ((Durable) punishment).setDuration(duration);
                }

                punishmentManager.getNonActive().add(punishment);

            }
            Utils.log("Loaded data from the database");
        } catch (SQLException exception) {
            Utils.log("Couldn't generate player data");
        }
    }

    public void saveTeams() {
        try (Connection connection = mySQL.getConnection()) {

            PreparedStatement activeStmt = connection.prepareStatement(
                    "INSERT INTO active_punishments (punisher, punished, reason, type, duration, remaining_duration, ban_time) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE type = VALUES(type), duration = VALUES(duration), remaining_duration = VALUES(remaining_duration), ban_time = VALUES(ban_time)");
            
            for (Punishment punishment : punishmentManager.getActivePunishments()) {

                activeStmt.setString(1, punishment.getPunisher().toString());
                activeStmt.setString(2, punishment.getPunished().toString());
                activeStmt.setString(3, punishment.getReason());
                activeStmt.setString(4, punishment.getPunishmentType().toString());
                activeStmt.setString(7, punishment.getDate());
                if (punishment instanceof Durable) {
                    Durable durable = (Durable) punishment;
                    activeStmt.setString(5, Utils.getDurationString(durable.getDuration()));

                    long remainingTime = (durable.getEndTime() - System.currentTimeMillis()) / 1000;
                    activeStmt.setString(6, Utils.getDurationString(remainingTime));
                } else {
                    activeStmt.setString(5, "0");
                    activeStmt.setString(6, "0");
                }
                activeStmt.executeUpdate();
                Utils.log("Saved data into the database: " + punishment);
            }


            PreparedStatement unactiveStmt = connection.prepareStatement(
                    "INSERT INTO unactive_punishments (punisher, punished, reason, type, duration, ban_time) VALUES (?, ?, ?, ?, ?, ?)");

            PreparedStatement deleteActiveStmt = connection.prepareStatement(
                    "DELETE FROM active_punishments WHERE punisher = ? AND punished = ? AND reason = ? AND type = ?");

            for (Punishment punishment : punishmentManager.getNonActive()) {

                unactiveStmt.setString(1, punishment.getPunisher().toString());
                unactiveStmt.setString(2, punishment.getPunished().toString());
                unactiveStmt.setString(3, punishment.getReason());
                unactiveStmt.setString(4, punishment.getPunishmentType().toString());
                unactiveStmt.setString(6, punishment.getDate());
                if (punishment instanceof Durable) {
                    Durable durable = (Durable) punishment;
                    unactiveStmt.setString(5, Utils.getDurationString(durable.getDuration()));
                } else {
                    unactiveStmt.setString(5, "0");
                }

                unactiveStmt.executeUpdate();

                deleteActiveStmt.setString(1, punishment.getPunisher().toString());
                deleteActiveStmt.setString(2, punishment.getPunished().toString());
                deleteActiveStmt.setString(3, punishment.getReason());
                deleteActiveStmt.setString(4, punishment.getPunishmentType().toString());
                deleteActiveStmt.executeUpdate();
            }
        } catch (SQLException e) {
            Utils.log("Can't find connection");
        }
    }

    public Punishment initPunishment(UUID punisher, UUID punished, String reason, PunishmentTypes type, long remaining, String date) {
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
