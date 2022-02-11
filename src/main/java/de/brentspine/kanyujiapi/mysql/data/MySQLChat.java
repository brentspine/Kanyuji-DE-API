package de.brentspine.kanyujiapi.mysql.data;

import de.brentspine.kanyujiapi.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLChat {

    public static boolean hasUserIgnored(UUID uuid, UUID ignored) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM playersIgnored WHERE uuid = ? AND ignored = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, ignored.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addUserIgnored(UUID uuid, UUID ignored) {
        if(hasUserIgnored(uuid, ignored)) return;
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("INSERT INTO playersIgnored (uuid, ignored) VALUES (?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, ignored.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeUserIgnored(UUID uuid, UUID ignored) {
        if(!hasUserIgnored(uuid, ignored)) return;
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM playersIgnored WHERE uuid = ? AND ignored = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, ignored.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<UUID> getAllUsersIgnored(UUID uuid) {
        ArrayList<UUID> r = new ArrayList<UUID>();
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM playersIgnored WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.add(UUID.fromString(rs.getString("ignored")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String getIgnoredSince(UUID uuid, UUID ignored) {
        if(!hasUserIgnored(uuid, ignored)) return "not_ignored";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT since FROM playersIgnored WHERE uuid = ? AND ignored = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, ignored.toString());
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) return "not_ignored";
            return rs.getString("since");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
