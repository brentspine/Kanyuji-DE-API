package de.brentspine.kanyujiapi;

import de.brentspine.kanyujiapi.commands.APITestCommand;
import de.brentspine.kanyujiapi.commands.CoinsCommand;
import de.brentspine.kanyujiapi.commands.FriendCommand;
import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.playtime.AFKListener;
import de.brentspine.kanyujiapi.playtime.Playtime;
import de.brentspine.kanyujiapi.commands.PlaytimeCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KanyujiAPI extends JavaPlugin {

    public static final String PREFIX = "[§4API§f] §7";
    public static final String NO_PERM = "§7Dazu hast du keine Rechte!";

    public static KanyujiAPI instance;
    public static Playtime playtime;
    public static AFKListener afkListener;
    public static FriendCommand friendCommand;

    public static void connectAll() {
        MySQL.connect();
        MySQL.connectGameModes();
    }

    public static void disconnectAll() {
        MySQL.disconnect();
        MySQL.disconnectGameModes();
    }

    @Override
    public void onEnable() {
        instance = this;
        playtime = new Playtime().run();
        afkListener = new AFKListener().run();
        friendCommand = new FriendCommand();
        register(Bukkit.getPluginManager());
    }

    public void register(PluginManager pluginManager) {
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("playtime").setExecutor(new PlaytimeCommand());
        getCommand("friends").setExecutor(friendCommand);
        getCommand("apitest").setExecutor(new APITestCommand());
        pluginManager.registerEvents(afkListener, this);
        pluginManager.registerEvents(friendCommand, this);
    }

    public static void connectData() {
        MySQL.connect();
    }

    public static void disconnectData() {
        MySQL.disconnect();
    }

    public static void connectGameModes() {
        MySQL.connectGameModes();
    }

    public static void disconnectGameModes() {
        MySQL.disconnectGameModes();
    }

    public Playtime getPlaytime() {
        return playtime;
    }

    public static String getNoPerm(String prefix) {
        return prefix + NO_PERM;
    }

}
