package me.psikuvit.betterpunishment.database.mysql;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public MySQL(BetterPunishment plugin) {
        this.host = plugin.getConfigUtils().getMySQLHost();
        this.port = plugin.getConfigUtils().getMySQLPort();
        this.database = plugin.getConfigUtils().getMySQLDatabase();
        this.username = plugin.getConfigUtils().getMySQLUser();
        this.password = plugin.getConfigUtils().getMySQLPassword();

        connectMySQL();
        registerMySQL();
    }

    // Connect to Database
    public void connectMySQL() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            Utils.log("Connected to MySQL");
        } catch (SQLException ex) {
            Utils.log("No valid MySQL Credentials is set in Config!\n Disabling Plugin!");
            Bukkit.getPluginManager().disablePlugin(BetterPunishment.getPlugin(BetterPunishment.class));
        }
    }

    // Setting up the Database
    public void registerMySQL() {
        try {
            if (isConnected()) {
                PreparedStatement activeStmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS active_punishments (punisher VARCHAR(100)" +
                        ", punished VARCHAR(100), reason VARCHAR(100), type VARCHAR(100), duration VARCHAR(100), remaining_duration VARCHAR(100), punishment_time VARCHAR(100))");
                activeStmt.executeUpdate();
                PreparedStatement nonactiveStmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS nonactive_punishments (punisher VARCHAR(100)" +
                        ", punished VARCHAR(100), reason VARCHAR(100), type VARCHAR(100), duration VARCHAR(100), punishment_time VARCHAR(100))");
                nonactiveStmt.executeUpdate();
            } else {
                Utils.log("Failed to register database: No connection");
            }
        } catch (SQLException e) {
            Utils.log("Failed to register database");
        }
    }

    // If the Server is connected to the MySQL
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException exception) {
            Utils.log("Couldn't check if database is connected");
            return false;
        }
    }

    // Get the Connection
    public Connection getConnection() {
        if (!isConnected()) {
            connectMySQL();
        }
        return connection;
    }


}
