package de.brentspine.kanyujiapi.playtime;

import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.data.MySQLPlaytime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Playtime {

    public static final String PREFIX = "§4§lPlaytime §8» §7";

    public Playtime run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Integer playersNotAFK = 0;
                for (Player current : Bukkit.getOnlinePlayers()) {
                    if(!KanyujiAPI.afkListener.afkPlayers.contains(current.getUniqueId())) {
                        MySQLPlaytime.addMinutesPlayed(current.getUniqueId(), 1);
                        MySQLPlaytime.updateLastOnline(current.getUniqueId());
                        playersNotAFK++;
                    }
                }
                Bukkit.getConsoleSender().sendMessage(PREFIX + "Added 1 Minute Playtime to Online Players (" + playersNotAFK + "/" + Bukkit.getOnlinePlayers().size() + ")");
            }
        }.runTaskTimer(KanyujiAPI.instance, 20 * 60, 20 * 60);
        return this;
    }

}
