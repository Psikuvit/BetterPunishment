package me.psikuvit.betterpunishment;

import me.psikuvit.betterpunishment.commands.CommandRegister;
import me.psikuvit.betterpunishment.database.MySQL;
import me.psikuvit.betterpunishment.database.MySQLData;
import me.psikuvit.betterpunishment.listeners.BanListener;
import me.psikuvit.betterpunishment.listeners.MuteListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterPunishment extends JavaPlugin {

    private PunishmentManager punishmentManager;
    private MySQL mySQL;
    private MySQLData mySQLData;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        punishmentManager = new PunishmentManager();
        mySQL = new MySQL(this);
        mySQLData = new MySQLData(this);

        mySQLData.loadTeams();


        getCommand("bp").setExecutor(new CommandRegister(this));
        getCommand("bp").setTabCompleter(new CommandRegister(this));

        getServer().getPluginManager().registerEvents(new BanListener(this), this);
        getServer().getPluginManager().registerEvents(new MuteListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQLData.saveTeams();
        mySQL.disconnectMySQL();
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public MySQLData getMySQLData() {
        return mySQLData;
    }
}
