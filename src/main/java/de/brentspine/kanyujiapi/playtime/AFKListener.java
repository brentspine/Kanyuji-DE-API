package de.brentspine.kanyujiapi.playtime;

import de.brentspine.kanyujiapi.KanyujiAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AFKListener implements Listener {

    public static final Integer AFK_TIME_SECONDS = 60;

    public static final String PREFIX = "§b§lLobby §8» §7";

    public ArrayList<UUID> afkPlayers = new ArrayList<>();
    public HashMap<UUID, Long> lastMoved = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        lastMoved.put(player.getUniqueId(), System.currentTimeMillis() + (long)(AFK_TIME_SECONDS * 1000));
        if(afkPlayers.contains(player.getUniqueId())) {
            afkPlayers.remove(player.getUniqueId());
            player.sendMessage(PREFIX + "§aDu bist nun nicht mehr AFK");
        }
    }

    public AFKListener run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player current : Bukkit.getOnlinePlayers()) {
                    if(lastMoved.containsKey(current.getUniqueId())) {
                        if(lastMoved.get(current.getUniqueId()) < System.currentTimeMillis() && !afkPlayers.contains(current.getUniqueId())) {
                            current.sendMessage(PREFIX + "§cDu bist nun AFK");
                            afkPlayers.add(current.getUniqueId());
                        }
                    } else {
                        lastMoved.put(current.getUniqueId(), System.currentTimeMillis() + (long)(AFK_TIME_SECONDS * 1000));
                    }
                }
            }
        }.runTaskTimer(KanyujiAPI.instance, 20, 20);
        return this;
    }

    public Long timeUntilAFK(Player player) {
        return lastMoved.get(player.getUniqueId()) - System.currentTimeMillis();
    }

}
