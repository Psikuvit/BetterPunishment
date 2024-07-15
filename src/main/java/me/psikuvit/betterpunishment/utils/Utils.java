package me.psikuvit.betterpunishment.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static void log(String msg) {
        Bukkit.getLogger().info(msg);
    }

    public static String getDurationString(long duration) {
        long days = TimeUnit.SECONDS.toDays(duration);
        duration -= TimeUnit.DAYS.toSeconds(days);
        long hours = TimeUnit.SECONDS.toHours(duration);
        duration -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(duration);
        duration -= TimeUnit.MINUTES.toSeconds(minutes);
        long seconds = TimeUnit.SECONDS.toSeconds(duration);
        StringBuilder result = new StringBuilder();
        if (days != 0L)
            result.append(days).append(" day(s) ");
        if (hours != 0L)
            result.append(hours).append(" hours(s) ");
        if (minutes != 0L)
            result.append(minutes).append(" minutes(s) ");
        if (seconds != 0L)
            result.append(seconds).append(" seconds(s)");
        return result.toString();
    }

    public static String getDate(long length) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(length * 1000L);
    }

    public static String getNowDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(currentDate);
    }

    public static long parse(String input) {
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (Character.isLetter(c) && (number.length() > 0)) {
                result += convert(Integer.parseInt(number.toString()), c);
                number = new StringBuilder();
            }
        }
        return result;
    }

    public static long convert(int value, char unit) {
        switch (unit) {
            case 'y':
                return (value * 31536000L);
            case 'M':
                return (value * 2592000L);
            case 'w':
                return (value * 604800L);
            case 'd':
                return (value * 86400L);
            case 'h':
                return (value * 3600L);
            case 'm':
                return (value * 60L);
            case 's':
                return (value);
        }
        return 0L;
    }
}
