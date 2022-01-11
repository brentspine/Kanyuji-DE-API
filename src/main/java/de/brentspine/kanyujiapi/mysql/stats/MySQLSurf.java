package de.brentspine.kanyujiapi.mysql.stats;

import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MySQLSurf {

    public static boolean isUserExisting(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("SELECT * FROM SurfStats WHERE uniqueID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean createUserIfNeeded(UUID uuid) {
        if(isUserExisting(uuid)) {
            return false;
        }
        try {
            PreparedStatement ps;
            ps = MySQL.gameCon.prepareStatement("INSERT INTO SurfStats (uniqueID, kills, deaths, points) VALUES (?, 0, 0, 0)");
            ps.setString(1, uuid.toString());
            ps.setInt(2, 0);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Integer getKills(UUID uuid) {
        if(!isUserExisting(uuid)) {
            return 0;
        }
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("SELECT kills FROM SurfStats WHERE uniqueID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("kills");
            }
        } catch (SQLException var1) {
            return 0;
        }
        return 0;
    }

    public static Integer getDeaths(UUID uuid) {
        if(!isUserExisting(uuid)) {
            return 0;
        }
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("SELECT deaths FROM SurfStats WHERE uniqueID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("deaths");
            }
        } catch (SQLException var1) {
            return 0;
        }
        return 0;
    }

    public static Integer getPoints(UUID uuid) {
        if(!MySQL.gameIsConnected()) {
            System.out.println("BRUH");
        }
        if(!isUserExisting(uuid)) {
            System.out.println("AAAAAAA");
            return 0;
        }
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("SELECT points FROM SurfStats WHERE uniqueID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static Integer getPosition(String name) {
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("SELECT uniqueID, kills, deaths, points, FIND_IN_SET( points, (    \n" +
                    "SELECT GROUP_CONCAT( points\n" +
                    "ORDER BY points DESC ) \n" +
                    "FROM SurfStats )\n" +
                    ") AS rank\n" +
                    "FROM SurfStats\n" +
                    "WHERE name =  ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("rank");
            } else
                return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private static HashMap<Integer, String> getTop(Integer amount, String criteria) {
        try {
            PreparedStatement ps = MySQL.gameCon.prepareStatement("select * from SurfStats order by ? desc limit ?;");
            ps.setString(1, criteria);
            ps.setInt(2, amount);
            ResultSet resultSet = ps.executeQuery();
            int ranking = 0;
            if (resultSet != null && !resultSet.wasNull()) {
                HashMap<Integer, String> r = new HashMap<>();
                while(resultSet.next()) {
                    ++ranking;
                    String r1 = resultSet.getString("uniqueID");
                    //Integer r2 = rs.getInt("minutesPlayed");
                    r.put(ranking, r1);
                    ranking++;
                }
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new HashMap<Integer, String>());
    }

    public static HashMap<Integer, String> getTopByKills(Integer amount) {
        return getTop(amount, "kills");
    }

    public static HashMap<Integer, String> getTopByDeaths(Integer amount) {
        return getTop(amount, "deaths");
    }

    public static HashMap<Integer, String> getTopByPoints(Integer amount) {
        return getTop(amount, "points");
    }



}
