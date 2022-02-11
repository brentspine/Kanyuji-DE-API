package de.brentspine.kanyujiapi.commands;

import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.mysql.data.MySQLCoins;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class CoinsCommand implements CommandExecutor {

    public static final String PREFIX = "§6§lCoins §8» §7";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (MySQL.isConnected()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UUID uuid;
                if (args.length == 0) {
                    try {
                        MySQLCoins.createUserIfNeeded(player.getUniqueId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage( PREFIX + "Du hast aktuell §c" + MySQLCoins.getFormatedPoints(player.getUniqueId()) + " §7Pixel");
                } else {
                    if (args[0].equalsIgnoreCase("help")) {
                        if (player.hasPermission("coins.help")) {
                            player.sendMessage(PREFIX + "- §c/pixel <user>");
                            player.sendMessage(PREFIX + "- §c/pixel set <spieler> <betrag>");
                            player.sendMessage(PREFIX + "- §c/pixel add <spieler> <betrag>");
                            player.sendMessage(PREFIX + "- §c/pixel remove <spieler> <betrag>");
                            player.sendMessage(PREFIX + "- §c/pixel reset <spieler>");
                        } else {
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                        }
                    } else if (args[0].equalsIgnoreCase("set")) {
                        if (player.hasPermission("coins.set")) {
                            if (args.length == 3) {
                                uuid = MySQLCoins.getUUIDByName(args[1]);
                                try {
                                    if(Integer.parseInt(args[2]) < Integer.MAX_VALUE) {
                                        if(MySQLCoins.createUserIfNeeded(uuid)) {
                                            player.sendMessage(PREFIX + "Es wurde ein Eintrag für Spieler §c" + args[1] + "§7 angelegt");
                                        }
                                        MySQLCoins.set(uuid, Integer.parseInt(args[2]));
                                        player.sendMessage(PREFIX + "Du hast den Kontostand des Spielers §c" + args[1] + "§7 auf §c" + Integer.parseInt(args[2]) + "§7 gesetzt");
                                    } else
                                        player.sendMessage(PREFIX + "Die Maximale Pixelanzahl ist §c2.000.000.000");
                                } catch (Exception e) {
                                    player.sendMessage(PREFIX + "Das ist keine Zahl / Deine zahl ist zu groß");
                                }
                            } else {
                                player.sendMessage(PREFIX + "Bitte benutze §c/pixel set <player> <amount>");
                            }
                        } else {
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                        }
                    } else if (args[0].equalsIgnoreCase("add")) {
                        if (player.hasPermission("coins.add")) {
                            if (args.length == 3) {
                                if(MySQLCoins.getUUIDByName(args[1]) != null) {
                                    uuid = MySQLCoins.getUUIDByName(args[1]);
                                    if(!MySQLCoins.isMaximumReached(uuid)) {
                                        try {
                                            MySQLCoins.createUserIfNeeded(uuid);
                                            if(Integer.parseInt(args[2]) < 2000000000) {
                                                if(MySQLCoins.getPoints(uuid) + Integer.parseInt(args[2]) < 2000000000) {
                                                    MySQLCoins.update(uuid, Integer.parseInt(args[2]), false);
                                                    player.sendMessage(PREFIX + "Dem Spieler §c" + args[1] + "§7 wurden §c" + Integer.parseInt(args[2]) + "§7 Coins hinzugefügt");
                                                } else {
                                                    MySQLCoins.set(uuid, 2000000000);
                                                    player.sendMessage(PREFIX + "Der Spieler §c" + args[1] + "§7 hatte bereits eine bestimmte Anzahl an Pixel, sodass der betrag nun bei §c2.000.000.000 §7ist!");
                                                }
                                            } else player.sendMessage(PREFIX + "Maximal 2 Milliarden");
                                        } catch (NumberFormatException | SQLException e) {
                                            player.sendMessage(PREFIX + "Das ist keine Zahl / Deine zahl ist zu groß");
                                        }
                                    } else player.sendMessage(PREFIX + "Der Spieler hat die Maximale anzahl an Pixel erreicht");
                                } else
                                    player.sendMessage(PREFIX + "Es scheint, als seie der Spieler §c" + args[1] + "§7 nicht in der Datenbank");
                            } else {
                                player.sendMessage(PREFIX + "Bitte benutze §c/pixel add <player> <amount>");
                            }
                        } else {
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                        }
                    } else if (args[0].equalsIgnoreCase("reset")) {
                        if (player.hasPermission("coins.reset")) {
                            if (args.length == 2) {
                                if(MySQLCoins.getUUIDByName(args[1]) != null) {
                                    if(MySQLCoins.isUserExisting(player.getUniqueId())) {
                                        MySQLCoins.set(player.getUniqueId(), 0);
                                    }
                                    player.sendMessage(PREFIX + "Du hast den Eintrag von §c" + player.getName() + "§7 entfernt");
                                } else
                                    player.sendMessage(PREFIX + "Es scheint, als seie der Spieler §c" + args[1] + "§7 nicht in der Datenbank");
                            } else {
                                player.sendMessage(PREFIX + "Bitte benutze §c/pixel reset <player>");
                            }
                        } else {
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (player.hasPermission("coins.remove")) {
                            if (args.length == 3) {
                                if(MySQLCoins.getUUIDByName(args[1]) != null) {
                                    uuid = MySQLCoins.getUUIDByName(args[1]);
                                    try {
                                        if (MySQLCoins.isUserExisting(uuid)) {
                                            MySQLCoins.update(uuid, Integer.parseInt(args[2]), true);
                                            player.sendMessage(PREFIX + "Dem Spieler §c" + args[1] + "§7 wurden §c" + Integer.parseInt(args[2]) + "§7 Coins entfernt");
                                        } else {
                                            player.sendMessage(PREFIX + "Der Spieler §c" + args[1] + "§7 ist nicht in der Datenbank!");
                                        }
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(PREFIX + "Das ist keine Zahl / Deine zahl ist zu groß");
                                    }
                                } else
                                    player.sendMessage(PREFIX + "Es scheint, als seie der Spieler §c" + args[1] + "§7 nicht in der Datenbank");
                            } else {
                                player.sendMessage(PREFIX + "Bitte benutze §c/pixel remove <player> <amount>");
                            }
                        } else {
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                        }
                    } else if (args[0].equalsIgnoreCase("check")){
                        if(player.hasPermission("coins.check")) {
                            if(args.length == 2) {
                                if(MySQLCoins.getUUIDByName(args[1]) != null) {
                                    uuid = MySQLCoins.getUUIDByName(args[1]);
                                    if(MySQLCoins.isUserExisting(uuid)) {
                                        player.sendMessage(PREFIX + "Der Spieler §c" + args[1] + "§7 hat aktuell §c" + MySQLCoins.getFormatedPoints(uuid) + " §7Pixel");
                                    } else
                                        player.sendMessage(PREFIX + "Der Spieler §c" + args[1] + "§7 steht nicht in der Datenbank!");
                                } else
                                    player.sendMessage(PREFIX + "Es scheint, als seie der Spieler §c" + args[1] + "§7 nicht in der Datenbank");
                            } else
                                player.sendMessage(PREFIX + "Bitte benutze §c/pixel check <player>");
                        } else
                            player.sendMessage(KanyujiAPI.getNoPerm(PREFIX));
                    } else {
                        if(player.hasPermission("coins.check")) {
                            if(args.length == 1) {
                                if(MySQLCoins.getUUIDByName(args[0]) != null) {
                                    uuid = MySQLCoins.getUUIDByName(args[0]);
                                    if(MySQLCoins.isUserExisting(uuid)) {
                                        player.sendMessage(PREFIX + "Der Spieler §c" + args[0] + "§7 hat aktuell §c" + MySQLCoins.getFormatedPoints(uuid) + " §7Pixel");
                                    } else
                                        player.sendMessage(PREFIX + "Der Spieler §c" + args[0] + "§7 hat aktuell §c0 §7Pixel");
                                } else
                                    player.sendMessage(PREFIX + "Es scheint, als würde der Spieler §c" + args[0] + "§7 nicht existieren");
                            }
                        } else
                            player.sendMessage(PREFIX + "Du hast aktuell §c" + MySQLCoins.getFormatedPoints(player.getUniqueId()) + " §7Pixel");
                    }
                }

            } else {
                sender.sendMessage(PREFIX + "Der Command kann nur als Spieler ausgeführt werden!");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(PREFIX + "Der Command konnte nicht ausgeführt werden §8(§cNo Connection§8)");
        }

        return false;
    }
}
