package de.brentspine.kanyujiapi.mysql.data;

import de.brentspine.kanyujiapi.mysql.MySQL;
import org.bukkit.Bukkit;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MySQLPlaytime {

    public static boolean isUserExisting(UUID uuid) {
        //Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "isUserExisting");
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT minutesPlayed FROM playtime WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean createUserIfNeeded(UUID uuid) {
        //Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "createUserIfNeeded");
        boolean isUserExisting = isUserExisting(uuid);
        if(!isUserExisting) {
            try {
                PreparedStatement ps;
                ps = MySQL.con.prepareStatement("INSERT INTO playtime (UUID, minutesPlayed) VALUES (?,?)");
                ps.setString(1, uuid.toString());
                ps.setInt(2, 0);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isUserExisting;
    }

    public static Integer getMinutesPlayed(UUID uuid) {
        //Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Get Minutes Played");
        if(!createUserIfNeeded(uuid)) {
            return 0;
        }
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT minutesPlayed FROM playtime WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("minutesPlayed");
            }
        } catch (SQLException var1) {
            return 0;
        }
        return 0;
    }


    public static String getFormattedTime(UUID uuid) {
        //Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "getFormattedTime");
        return formatTime(getMinutesPlayed(uuid));
    }

    public static String formatTime(Integer minutes) {
        //Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "getFormattedTime");
        Integer minutesPlayed = minutes;
        int hoursPlayed = 0;
        int daysPlayed = 0;
        while (minutesPlayed > 59) {
            hoursPlayed++;
            minutesPlayed = minutesPlayed - 60;
        }
        while (hoursPlayed > 23) {
            daysPlayed++;
            hoursPlayed = hoursPlayed - 24;
        }
        if(daysPlayed > 0) {
            return daysPlayed + " Days " + hoursPlayed + " Hours";
        }
        else if(hoursPlayed > 0) {
            return hoursPlayed + " Hours";
        } else
            return minutesPlayed + " Minutes";

    }


    public static void addMinutesPlayed(UUID uuid, Integer amount) {
        createUserIfNeeded(uuid);
        int minutesPlayed = getMinutesPlayed(uuid);
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("UPDATE playtime SET minutesPlayed = ? WHERE UUID = ?");
            ps.setInt(1, minutesPlayed + amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeMinutesPlayed(UUID uuid, Integer amount) {
        createUserIfNeeded(uuid);
        int minutesPlayed = getMinutesPlayed(uuid);
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("UPDATE playtime SET minutesPlayed = ? WHERE UUID = ?");
            ps.setInt(1, minutesPlayed - amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void set(UUID uuid, Integer amount) {
        createUserIfNeeded(uuid);
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("UPDATE playtime SET minutesPlayed = ? WHERE UUID = ?");
            ps.setInt(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reset(UUID uuid) {
        if(!isUserExisting(uuid)) {
            return;
        }
        set(uuid, 0);
    }

    public static Integer updateLastOnline(UUID uuid) {
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("UPDATE playtime SET lastOnline = ? WHERE UUID = ?");
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public static Timestamp getLastOnline(UUID uuid) {
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("SELECT lastOnline FROM playtime WHERE UUID = ?");
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getTimestamp("lastOnline");
            }
        } catch (SQLException e) {
            return Timestamp.from(Instant.MIN);
        }
        return Timestamp.from(Instant.MIN);
    }

    public static HashMap<Integer, String> getTopPlayers(Integer amount) {
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("SELECT * FROM playtime ORDER BY minutesPlayed DESC LIMIT ?;");
            ps.setInt(1, amount);
            ResultSet rs = ps.executeQuery();
            HashMap<Integer, String> r = new HashMap<>();
            Integer i = 0;
            while (rs.next()) {
                String r1 = rs.getString("uuid");
                //Integer r2 = rs.getInt("minutesPlayed");
                r.put(i, r1);
                i++;
            }
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static UUID getUUIDByName(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }


}
