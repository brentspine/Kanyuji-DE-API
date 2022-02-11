package de.brentspine.kanyujiapi.mysql;

import de.brentspine.kanyujiapi.KanyujiAPI;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static String host = "localhost";
    public static String port = "3306";
    public static String database = "data";
    public static String gameDatabase = "stats";
    public static String user = "root";
    public static String password = "craftmine-01";
    public static Connection con;
    public static Connection gameCon;

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Es wurde eine Verbindung mit der Datenbank§8(§c" + database + "§8) §7aufgebaut §8(§c" + host + "§7:§c" + port + "§8)");
            } catch (SQLException var1) {
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Es konnte keine Verbindung mit der Datenbank aufgebaut werden");
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Verbundung geschlossen");
            } catch (SQLException var1) {
                var1.printStackTrace();
            }
        }
    }

    public static void connectGameModes() {
        if(!gameIsConnected()) {
            try {
                gameCon = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + gameDatabase, user, password);
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Es wurde eine Verbindung mit der Gamemode Datenbank§8(§c" + database + "§8) §7aufgebaut §8(§c" + host + "§7:§c" + port + "§8)");
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Es konnte keine Verbindung mit der GameMode Datenbank aufgebaut werden");
            }
        }
    }

    public static void disconnectGameModes() {
        if(gameIsConnected()) {
            try {
                gameCon.close();
                Bukkit.getConsoleSender().sendMessage(KanyujiAPI.PREFIX + "Gamemode Datenbank Verbindung geschlossen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public static boolean isConnected() {
        return con != null;
    }

    public static Connection getConnection() {
        return con;
    }

    public static boolean gameIsConnected() {
        return gameCon != null;
    }

    public static Connection getGameConnection() {
        return gameCon;
    }

}
