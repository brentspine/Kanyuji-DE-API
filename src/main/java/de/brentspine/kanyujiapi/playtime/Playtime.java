package de.brentspine.kanyujiapi.playtime;

import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.MySQLPlaytime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Playtime {

    public static final String PREFIX = "§4§lPlaytime §8» §7";

    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player current : Bukkit.getOnlinePlayers()) {
                    MySQLPlaytime.addMinutesPlayed(current.getUniqueId(), 1);
                }
                Bukkit.getConsoleSender().sendMessage(PREFIX + "Added 1 Minute Playtime to Online Players (" + Bukkit.getOnlinePlayers().size() + ")");
            }
        }.runTaskTimer(KanyujiAPI.instance, 20 * 60, 20 * 60);
    }

}
