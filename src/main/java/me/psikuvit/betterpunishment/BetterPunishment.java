package me.psikuvit.betterpunishment;

import me.psikuvit.betterpunishment.commands.CommandRegister;
import me.psikuvit.betterpunishment.database.Database;
import me.psikuvit.betterpunishment.database.local.LocalData;
import me.psikuvit.betterpunishment.database.mysql.MySQLData;
import me.psikuvit.betterpunishment.listeners.BanListener;
import me.psikuvit.betterpunishment.listeners.MuteListener;
import me.psikuvit.betterpunishment.utils.ConfigUtils;
import me.psikuvit.betterpunishment.utils.DatabaseType;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterPunishment extends JavaPlugin {

    private PunishmentManager punishmentManager;
    private ConfigUtils configUtils;
    private Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        punishmentManager = new PunishmentManager();
        configUtils = new ConfigUtils(this);

        if (configUtils.getDatabaseType() == DatabaseType.MYSQL) {
            database = new MySQLData(this);
        } else if (configUtils.getDatabaseType() == DatabaseType.LOCAL) {
            database = new LocalData(this);
        }

        database.loadData();


        getCommand("bp").setExecutor(new CommandRegister(this));
        getCommand("bp").setTabCompleter(new CommandRegister(this));

        getServer().getPluginManager().registerEvents(new BanListener(this), this);
        getServer().getPluginManager().registerEvents(new MuteListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        database.saveData();
        if (configUtils.getDatabaseType() == DatabaseType.MYSQL) {
            MySQLData mySQLData = (MySQLData) database;
            mySQLData.disconnectMySQL();
        }

    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public Database getDatabase() {
        return database;
    }
}
