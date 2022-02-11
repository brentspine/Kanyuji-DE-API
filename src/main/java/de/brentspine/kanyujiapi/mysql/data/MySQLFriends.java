package de.brentspine.kanyujiapi.mysql.data;

import de.brentspine.kanyujiapi.mysql.MySQL;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MySQLFriends {

    //Returns true if there is any entry for the user
    public static boolean isUserExisting(UUID uuid) {
        try {
            PreparedStatement ps1 = MySQL.con.prepareStatement("SELECT user1 FROM friends WHERE user1 = ?");
            ps1.setString(1, uuid.toString());
            ResultSet rs1 = ps1.executeQuery();

            if(rs1.next()) {
                return true;
            }

            PreparedStatement ps2 = MySQL.con.prepareStatement("SELECT user2 FROM friends WHERE user2 = ?");
            ps2.setString(1, uuid.toString());
            ResultSet rs2 = ps2.executeQuery();

            return rs2.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Returns true if users are already friends
    public static boolean areFriends(UUID uuid, UUID uuid2) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM friends WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?) LIMIT 1");
            ps.setString(1, uuid.toString());
            ps.setString(2, uuid2.toString());
            ps.setString(3, uuid2.toString());
            ps.setString(4, uuid.toString());
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Returns true if success
    public static boolean addFriends(UUID uuid, UUID uuid2) {
        try {
            if(areFriends(uuid, uuid2)) {
                return false;
            }
            PreparedStatement ps = MySQL.con.prepareStatement("INSERT INTO friends (user1, user2) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, uuid2.toString());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //Returns true if success
    public static boolean removeFriends(UUID uuid, UUID uuid2) {
        try {

            PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM friends WHERE (user1 = '" + uuid + "' AND user2 = '" + uuid2 + "') OR (user1 = '" + uuid2 + "' AND user2 = '" + uuid + "') LIMIT 1");
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Returns an ArrayList with all UUIDs of every friend
    public static ArrayList<UUID> getAllFriends(UUID uuid) {
        ArrayList<UUID> r = new ArrayList<>();
        ResultSet rs1;
        ResultSet rs2;
        try {
            PreparedStatement ps1 = MySQL.con.prepareStatement("SELECT * FROM friends WHERE user2 = ?");
            ps1.setString(1, uuid.toString());
            rs1 = ps1.executeQuery();

            PreparedStatement ps2 = MySQL.con.prepareStatement("SELECT * FROM friends WHERE user1 = ?");
            ps2.setString(1, uuid.toString());
            rs2 = ps2.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return r;
        }

        try {
            while (rs1.next()) {
                r.add(UUID.fromString(rs1.getString("user1")));
            }
        } catch (SQLException e) {}

        try {
            while (rs2.next()) {
                r.add(UUID.fromString(rs2.getString("user2")));
            }
        } catch (SQLException e) {}

        return r;

    }

    //Returns the Timestamp for the time the friendship was created
    public static Timestamp getFriendsSince(UUID uuid, UUID uuid2) {
        ResultSet rs1;
        ResultSet rs2;
        try {
            PreparedStatement ps1 = MySQL.con.prepareStatement("SELECT * FROM friends WHERE user1 = ? AND user2 = ?");
            ps1.setString(1, uuid.toString());
            ps1.setString(2, uuid2.toString());
            rs1 = ps1.executeQuery();

            PreparedStatement ps2 = MySQL.con.prepareStatement("SELECT * FROM friends WHERE user2 = ? AND user1 = ?");
            ps2.setString(1, uuid.toString());
            ps2.setString(2, uuid2.toString());
            rs2 = ps2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if(rs1.next()) {
                return rs1.getTimestamp("friendsSince");
            }
        } catch (SQLException e) {}

        try {
            if(rs2.next()) {
                return rs2.getTimestamp("friendsSince");
            }
        } catch (SQLException e) {}

        return null;
    }

    public static Integer createFriendRequest(UUID uuid, UUID from) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("INSERT INTO friendRequests (uuid, madeBy) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, from.toString());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Integer removeFriendRequest(UUID uuid, UUID from) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM friendRequests WHERE uuid = ? AND madeBy = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, from.toString());

            PreparedStatement ps2 = MySQL.con.prepareStatement("DELETE FROM friendRequests WHERE uuid = ? AND madeBy = ?");
            ps2.setString(2, uuid.toString());
            ps2.setString(1, from.toString());

            return ps.executeUpdate() + ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static HashMap<UUID, Timestamp> getFriendRequests(UUID uuid) {
        HashMap<UUID, Timestamp> r = new HashMap<>();
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM friendRequests WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                r.put(UUID.fromString(rs.getString("madeBy")), rs.getTimestamp("createdAt"));
            }
            return r;
        } catch (SQLException e) {
            return r;
        }
    }

    public static boolean hasFriendRequest(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM friendRequests WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean hasFriendRequest(UUID uuid, UUID from) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM friendRequests WHERE uuid = ? AND madeBy = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, from.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }


}
