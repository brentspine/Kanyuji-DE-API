package de.brentspine.kanyujiapi.mysql.data;

import de.brentspine.kanyujiapi.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLLootChests {

    public static boolean isUserExisting(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT uuid FROM chestKeys WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    //Returns true if the user already has an entry
    //Creates a user entry for the given uuid, when no entry exists
    public static boolean createUserIfNeeded(UUID uuid) {
        boolean isUserExisting = isUserExisting(uuid);
        if(!isUserExisting) {
            try {
                PreparedStatement ps;
                ps = MySQL.con.prepareStatement("INSERT INTO chestKeys (UUID, scratchOff, dices, slots, universal) VALUES (?,?,?,?,?)");
                ps.setString(1, uuid.toString());
                ps.setInt(2, 0);
                ps.setInt(3, 0);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isUserExisting;
    }

    public static int getScratchOffs(UUID uuid) {
        return getInt(uuid, "scratchOff");
    }

    public static int getDices(UUID uuid) {
        return getInt(uuid, "dices");
    }

    public static int getSlots(UUID uuid) {
        return getInt(uuid, "slots");
    }

    public static int getUniversal(UUID uuid) {
        return getInt(uuid, "universal");
    }

    public static void addScratchOffs(UUID uuid, int amount) {
        addInt(uuid, "scratchOff", amount);
    }

    public static void addDices(UUID uuid, int amount) {
        addInt(uuid, "dices", amount);
    }

    public static void addSlots(UUID uuid, int amount) {
        addInt(uuid, "slots", amount);
    }

    public static void addUniversal(UUID uuid, int amount) {
        addInt(uuid, "universal", amount);
    }

    public static void setScratchOffs(UUID uuid, int amount) {
        setInt(uuid, "scratchOff", amount);
    }

    public static void setDices(UUID uuid, int amount) {
        setInt(uuid, "dices", amount);
    }

    public static void setSlots(UUID uuid, int amount) {
        setInt(uuid, "slots", amount);
    }

    public static void setUniversal(UUID uuid, int amount) {
        setInt(uuid, "universal", amount);
    }

    public static void addAll(UUID uuid, int amount) {
        addScratchOffs(uuid, amount);
        addDices(uuid, amount);
        addSlots(uuid, amount);
        addUniversal(uuid, amount);
    }

    //Selects and returns the integer entry for the given string in the table,
    //made to avoid duplicate code and just use getInt in the public methods
    private static int getInt(UUID uuid, String column) {
        if(!createUserIfNeeded(uuid)) return 0;
        try {
            PreparedStatement ps;
            ps = MySQL.con.prepareStatement("SELECT " + column + " FROM chestKeys WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Sets the given int amount for the given column
    //made to avoid duplicate code, the public methods will use this
    public static void setInt(UUID uuid, String column, int amount) {
        if(createUserIfNeeded(uuid)) {
            try {
                PreparedStatement ps;
                ps = MySQL.con.prepareStatement("UPDATE chestKeys SET " + column + " = ? WHERE uuid = ?");
                ps.setInt(1, amount);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Adds the given int amount in the for the given column
    //made to avoid duplicate code, the public methods will use this
    public static void addInt(UUID uuid, String column, int amount) {
        if(createUserIfNeeded(uuid)) {
            int currentValue = getInt(uuid, column);
            try {
                PreparedStatement ps;
                ps = MySQL.con.prepareStatement("UPDATE chestKeys SET " + column + " = ? WHERE uuid = ?");
                ps.setInt(1, currentValue + amount);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Resets every column for the given uuid by deleting it and then creating a new one
    public static void reset(UUID uuid) {
        if(!isUserExisting(uuid)) return;
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM chestKeys WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createUserIfNeeded(uuid);
    }





}
