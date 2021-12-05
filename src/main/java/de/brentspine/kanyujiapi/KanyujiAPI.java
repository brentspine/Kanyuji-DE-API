package de.brentspine.kanyujiapi;

import de.brentspine.kanyujiapi.coins.CoinsCommand;
import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.mysql.stats.MySQLSurf;
import de.brentspine.kanyujiapi.playtime.Playtime;
import de.brentspine.kanyujiapi.playtime.PlaytimeCommand;
import de.brentspine.kanyujiapi.stats.DevTestCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KanyujiAPI extends JavaPlugin {

    public static final String PREFIX = "[§4API§f] §7";
    public static final String NO_PERM = "§7Dazu hast du keine Rechte!";

    public static KanyujiAPI instance;
    private Playtime playtime;

    public static void start() {
        MySQL.connect();
        MySQL.connectGameModes();
    }

    public static void stop() {
        MySQL.disconnect();
    }

    @Override
    public void onEnable() {
        instance = this;
        playtime = new Playtime();
        playtime.run();
        register(Bukkit.getPluginManager());
    }

    public void register(PluginManager pluginManager) {
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("playtime").setExecutor(new PlaytimeCommand());
        getCommand("devtest").setExecutor(new DevTestCommand());
    }

    public Playtime getPlaytime() {
        return playtime;
    }

    public static String getNoPerm(String prefix) {
        return prefix + NO_PERM;
    }

}
