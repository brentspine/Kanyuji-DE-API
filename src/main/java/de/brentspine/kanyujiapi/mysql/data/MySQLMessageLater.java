package de.brentspine.kanyujiapi.mysql.data;

import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.util.DirectMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;


public class MySQLMessageLater {

    public static boolean isTargetExisting(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT target FROM messagelater WHERE target = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean isSenderExisting(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT sender FROM messagelater WHERE sender = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static ArrayList<DirectMessage> getMessages(UUID target) {
        ArrayList<DirectMessage> r = new ArrayList<>();
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT target,sender,message FROM messagelater WHERE target = ?");
            ps.setString(1, target.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DirectMessage dm = new DirectMessage()
                        .setTarget(rs.getString("target"))
                        .setSender(rs.getString("sender"))
                        .setMessage(rs.getString("message"));
                r.add(dm);
            }
            return r;
        } catch (SQLException e) {
            return r;
        }
    }

    public static Integer createMessage(UUID target, UUID sender, String message) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("INSERT INTO messagelater (target, sender, message) VALUES (?, ?, ?)");
            ps.setString(1, target.toString());
            ps.setString(2, sender.toString());
            ps.setString(3, message);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList<DirectMessage> getMessagesSentBy(UUID sender) {
        ArrayList<DirectMessage> r = new ArrayList<>();
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT target,sender,message FROM messagelater WHERE sender = ?");
            ps.setString(1, sender.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DirectMessage dm = new DirectMessage()
                        .setTarget(rs.getString("target"))
                        .setSender(rs.getString("sender"))
                        .setMessage(rs.getString("message"));
                r.add(dm);
            }
            return r;
        } catch (SQLException e) {
            return r;
        }
    }

    public static Integer removeAllMessages(UUID target) {
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM `messagelater` WHERE target = ?");
            ps.setString(1, target.toString());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }


}
