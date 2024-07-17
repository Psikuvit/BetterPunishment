package me.psikuvit.betterpunishment.database.mysql;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.PunishmentManager;
import me.psikuvit.betterpunishment.database.Database;
import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLData implements Database {

    private final MySQL mySQL;
    private final PunishmentManager punishmentManager;

    public MySQLData(BetterPunishment plugin) {
        super();
        this.mySQL = new MySQL(plugin);
        this.punishmentManager = plugin.getPunishmentManager();
    }

    @Override
    public void loadData() {
        try (Connection connection = mySQL.getConnection()) {

            PreparedStatement activeStmt = connection.prepareStatement("SELECT punisher, punished, reason, type, duration, remaining_duration, punishment_time FROM active_punishments");
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

            PreparedStatement nonactiveStmt = connection.prepareStatement("SELECT punisher, punished, reason, type, duration, punishment_time FROM nonactive_punishments");
            ResultSet nonactiveSet = nonactiveStmt.executeQuery();

            while (nonactiveSet.next()) {
                UUID punisher = UUID.fromString(nonactiveSet.getString("punisher"));
                UUID punished = UUID.fromString(nonactiveSet.getString("punished"));

                String reason = nonactiveSet.getString("reason");

                String typeString = nonactiveSet.getString("type");
                PunishmentTypes type = PunishmentTypes.valueOf(typeString);

                String durationString = nonactiveSet.getString("duration");
                long duration = Utils.parse(durationString);

                String banTime = nonactiveSet.getString("ban_time");

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

    @Override
    public void saveData() {
        try (Connection connection = mySQL.getConnection()) {

            PreparedStatement activeStmt = connection.prepareStatement(
                    "INSERT INTO active_punishments (punisher, punished, reason, type, duration, remaining_duration, punishment_time) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE type = VALUES(type), duration = VALUES(duration), remaining_duration = VALUES(remaining_duration), punishment_time = VALUES(punishment_time)");
            
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


            PreparedStatement nonactiveStmt = connection.prepareStatement(
                    "INSERT INTO nonactive_punishments (punisher, punished, reason, type, duration, punishment_time) VALUES (?, ?, ?, ?, ?, ?)");

            PreparedStatement deleteActiveStmt = connection.prepareStatement(
                    "DELETE FROM active_punishments WHERE punisher = ? AND punished = ? AND reason = ? AND type = ?");

            for (Punishment punishment : punishmentManager.getNonActive()) {

                nonactiveStmt.setString(1, punishment.getPunisher().toString());
                nonactiveStmt.setString(2, punishment.getPunished().toString());
                nonactiveStmt.setString(3, punishment.getReason());
                nonactiveStmt.setString(4, punishment.getPunishmentType().toString());
                nonactiveStmt.setString(6, punishment.getDate());
                if (punishment instanceof Durable) {
                    Durable durable = (Durable) punishment;
                    nonactiveStmt.setString(5, Utils.getDurationString(durable.getDuration()));
                } else {
                    nonactiveStmt.setString(5, "0");
                }

                nonactiveStmt.executeUpdate();

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

    public void disconnectMySQL() {
        try {
            if (mySQL.getConnection() != null && !mySQL.getConnection().isClosed()) {
                mySQL.getConnection().close();
                Utils.log("Disconnected from MySQL");
            }
        } catch (SQLException exception) {
            Utils.log("Couldn't disconnect from database");
        }
    }
}
