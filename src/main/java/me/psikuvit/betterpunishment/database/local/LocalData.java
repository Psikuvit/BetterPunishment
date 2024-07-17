package me.psikuvit.betterpunishment.database.local;

import me.psikuvit.betterpunishment.BetterPunishment;
import me.psikuvit.betterpunishment.PunishmentManager;
import me.psikuvit.betterpunishment.database.Database;
import me.psikuvit.betterpunishment.punishements.interfaces.Durable;
import me.psikuvit.betterpunishment.punishements.interfaces.Punishment;
import me.psikuvit.betterpunishment.utils.PunishmentTypes;
import me.psikuvit.betterpunishment.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class LocalData implements Database {

    private final File activeFile;
    private final File nonactiveFile;
    private final PunishmentManager punishmentManager;

    public LocalData(BetterPunishment plugin) {
        this.activeFile = new File(plugin.getDataFolder() + "/punishments/active.yml");
        this.nonactiveFile = new File(plugin.getDataFolder() + "/punishments/nonactive.yml");
        this.punishmentManager = plugin.getPunishmentManager();
        initFiles();

    }

    @Override
    public void loadData() {
        FileConfiguration activeConfig = YamlConfiguration.loadConfiguration(activeFile);
        FileConfiguration nonactiveConfig = YamlConfiguration.loadConfiguration(activeFile);

        for (String key : activeConfig.getKeys(false)) {
            UUID punishedUUID = UUID.fromString(key);
            UUID punisherUUID = UUID.fromString(Objects.requireNonNull(activeConfig.getString(key + ".punisher")));

            String reason = activeConfig.getString(key + ".reason");
            PunishmentTypes type = PunishmentTypes.valueOf(activeConfig.getString(key + ".type"));

            String date = activeConfig.getString(key + ".date");

            String durationStr = Objects.requireNonNull(activeConfig.getString(key + ".duration"));
            long duration = Utils.parse(durationStr);

            String remainingTimeStr = Objects.requireNonNull(activeConfig.getString(key + ".remainingTime"));
            long remainingTime = Utils.parse(remainingTimeStr);

            Punishment punishment = initPunishment(punisherUUID, punishedUUID, reason, type, remainingTime, date);
            if (punishment instanceof Durable) {
                ((Durable) punishment).setDuration(duration);
            }

            punishmentManager.getActivePunishments().add(punishment);

        }

        for (String key : nonactiveConfig.getKeys(false)) {
            UUID punishedUUID = UUID.fromString(key);
            UUID punisherUUID = UUID.fromString(Objects.requireNonNull(activeConfig.getString(key + ".punisher")));

            String reason = activeConfig.getString(key + ".reason");
            PunishmentTypes type = PunishmentTypes.valueOf(activeConfig.getString(key + ".type"));

            String date = activeConfig.getString(key + ".date");

            String durationStr = Objects.requireNonNull(activeConfig.getString(key + ".duration"));
            long duration = Utils.parse(durationStr);

            Punishment punishment = initPunishment(punisherUUID, punishedUUID, reason, type, duration, date);
            if (punishment instanceof Durable) {
                ((Durable) punishment).setDuration(duration);
            }

            punishmentManager.getNonActive().add(punishment);

        }

    }

    @Override
    public void saveData() {
        FileConfiguration activeConfig = YamlConfiguration.loadConfiguration(activeFile);
        FileConfiguration nonactiveConfig = YamlConfiguration.loadConfiguration(activeFile);

        clearFile(activeFile);
        clearFile(nonactiveFile);


        for (Punishment punishment : punishmentManager.getActivePunishments()) {
            String path = punishment.getPunished().toString();

            activeConfig.set(path + ".punisher", punishment.getPunisher().toString());
            activeConfig.set(path + ".reason", punishment.getReason());
            activeConfig.set(path + ".type", punishment.getPunishmentType().toString());
            activeConfig.set(path + ".date", punishment.getDate());

            if (punishment instanceof Durable) {
                Durable durable = (Durable) punishment;
                activeConfig.set(path + ".duration", Utils.getDurationString(durable.getDuration()));

                long remainingTime = (durable.getEndTime() - System.currentTimeMillis()) / 1000;
                activeConfig.set(path + ".remainingTime", Utils.getDurationString(remainingTime));
            } else {
                activeConfig.set(path + ".duration", "0");
                activeConfig.set(path + ".remainingTime", "0");
            }
            try {
                activeConfig.save(activeFile);
                Utils.log("Saved data into the database: " + punishment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        for (Punishment punishment : punishmentManager.getNonActive()) {
            String path = punishment.getPunished().toString();
            nonactiveConfig.set(path + ".punisher", punishment.getPunisher().toString());
            nonactiveConfig.set(path + ".reason", punishment.getReason());
            nonactiveConfig.set(path + ".type", punishment.getPunishmentType().toString());
            nonactiveConfig.set(path + ".date", punishment.getDate());
            if (punishment instanceof Durable) {
                Durable durable = (Durable) punishment;
                nonactiveConfig.set(path + ".duration", Utils.getDurationString(durable.getDuration()));
            } else {
                nonactiveConfig.set(path + ".duration", "0");
            }
            try {
                nonactiveConfig.save(nonactiveFile);
                Utils.log("Saved data into the database: " + punishment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initFiles() {
        if (!activeFile.exists()) {
            activeFile.mkdirs();
        }
        if (!nonactiveFile.exists()) {
            nonactiveFile.mkdirs();
        }
    }

    public void clearFile(File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
