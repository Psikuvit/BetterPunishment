package me.psikuvit.betterpunishment.utils;

import me.psikuvit.betterpunishment.BetterPunishment;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigUtils {

    private final FileConfiguration fileConfiguration;
    private final File configFile;

    public ConfigUtils(BetterPunishment plugin) {
        this.fileConfiguration = plugin.getConfig();
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public DatabaseType getDatabaseType() {
        String databaseString = fileConfiguration.getString("database");
        return DatabaseType.valueOf(databaseString);
    }

    public String getMySQLHost() {
        return fileConfiguration.getString("mysql.host");
    }

    public String getMySQLPort() {
        return fileConfiguration.getString("mysql.port");
    }

    public String getMySQLDatabase() {
        return fileConfiguration.getString("mysql.database");
    }

    public String getMySQLUser() {
        return fileConfiguration.getString("mysql.user");
    }

    public String getMySQLPassword() {
        return fileConfiguration.getString("mysql.password");
    }

}
