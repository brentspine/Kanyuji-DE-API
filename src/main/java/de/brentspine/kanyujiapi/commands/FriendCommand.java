package de.brentspine.kanyujiapi.commands;

import de.brentspine.kanyujiapi.mysql.MySQL;
import de.brentspine.kanyujiapi.mysql.data.MySQLFriends;
import de.brentspine.kanyujiapi.mysql.data.MySQLMessageLater;
import de.brentspine.kanyujiapi.util.TextComponentBuilder;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FriendCommand implements CommandExecutor, Listener {

    public static final String PREFIX = "§a§lFriends §8» §7";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(PREFIX + "InGame only");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0) {
            String message = PREFIX + "§2§lYour Friend List:";
            for(UUID uuid : MySQLFriends.getAllFriends(player.getUniqueId())) {
                message = message + "\n" + PREFIX + "§c" + UUIDFetcher.getNameWithOfflinePlayer(uuid) + "§7 since " + MySQLFriends.getFriendsSince(uuid, player.getUniqueId());
            }
            player.sendMessage(message);
            return true;
        }

        UUID target;
        UUID uuid;

        switch (args[0]) {
            case "add":
                if(args.length < 2) {
                    player.sendMessage(PREFIX + "§cUsage: /friend add <Name>");
                    return true;
                }
                target = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
                if(MySQLFriends.areFriends(target, uuid)) {
                    player.sendMessage(PREFIX + "You are already friends since §c" + MySQLFriends.getFriendsSince(uuid, target));
                    return true;
                }
                if(MySQLFriends.hasFriendRequest(uuid, target)) {
                    MySQLFriends.addFriends(uuid, target);
                    return true;
                }
                MySQLFriends.createFriendRequest(target, uuid);
                player.sendMessage(PREFIX + "Du hast §c" + args[1] + "§7 eine Freundschaftsanfrage gesendet");
                for(Player current : Bukkit.getOnlinePlayers()) {
                    if(current.getUniqueId() == target) {
                        final ComponentBuilder message = new ComponentBuilder(PREFIX);

                        message.append(ChatColor.GREEN.toString() + ChatColor.BOLD + "[Accept] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept " + player.getName()));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to accept!")));

                        message.append(ChatColor.RED.toString() + ChatColor.BOLD + "[Decline] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends decline " + player.getName()));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to decline!")));

                        message.append(ChatColor.GRAY.toString() + ChatColor.BOLD + "[Ignore] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends ignore " + player.getName()));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to ignore")));

                        current.sendMessage(PREFIX + "§lYou got a friend request from §c§l" + player.getName());
                        current.spigot().sendMessage(message.create());
                        return true;
                    }
                }
                break;



            case "accept":
                if (args.length < 2) {
                    player.sendMessage(PREFIX + "§cUsage: /friend accept <Name>");
                    return true;
                }
                target = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
                if(MySQLFriends.areFriends(target, uuid)) {
                    player.sendMessage(PREFIX + "You are already friends since §c" + MySQLFriends.getFriendsSince(uuid, target));
                    return true;
                }
                if(!MySQLFriends.hasFriendRequest(uuid, target)) {
                    player.sendMessage(PREFIX + "Du hast keine Anfrage von §c" + UUIDFetcher.getNameWithOfflinePlayer(target));
                    return true;
                }
                MySQLFriends.addFriends(uuid, target);
                MySQLFriends.removeFriendRequest(uuid, target);
                player.sendMessage(PREFIX + "Du hast die Anfrage von §c" + args[1] + "§7 angenommen!");
                MySQLMessageLater.createMessage(target, UUID.fromString("291af7c7-2114-45bb-a97a-d3b4077392e8"), PREFIX + "§e" + player.getName() + " accepted your Friend Request");
                break;

            case "decline":
                if (args.length < 2) {
                    player.sendMessage(PREFIX + "§cUsage: /friend decline <Name>");
                    return true;
                }
                target = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
                if(!MySQLFriends.hasFriendRequest(uuid, target)) {
                    player.sendMessage(PREFIX + "Du hast keine Anfrage von §c" + UUIDFetcher.getNameWithOfflinePlayer(target));
                    return true;
                }
                MySQLFriends.removeFriendRequest(uuid, target);
                player.sendMessage(PREFIX + "Du hast die Anfrage von §c" + args[1] + "§7 abgelehnt!");
                break;

            case "ignore":
                if (args.length < 2) {
                    player.sendMessage(PREFIX + "§cUsage: /friend decline <Name>");
                    return true;
                }
                target = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
                if(!MySQLFriends.hasFriendRequest(uuid, target)) {
                    player.sendMessage(PREFIX + "Du hast keine Anfrage von §c" + UUIDFetcher.getNameWithOfflinePlayer(target));
                    return true;
                }
                MySQLFriends.removeFriendRequest(uuid, target);
                player.sendMessage(PREFIX + "Du hast die Anfrage von " + args[1] + " ignoriert");
                break;

            case "remove":
                if (args.length < 2) {
                    player.sendMessage(PREFIX + "§cUsage: /friend decline <Name>");
                    return true;
                }
                target = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
                uuid = player.getUniqueId();
                if(!MySQLFriends.areFriends(target, uuid)) {
                    player.sendMessage(PREFIX + "You aren't friends!");
                    return true;
                }
                MySQLFriends.removeFriends(uuid, target);
                player.sendMessage(PREFIX + "Du hast §c" + args[1] + "§7 entfernt");
                break;

            case "requests":
                uuid = player.getUniqueId();
                HashMap<UUID, Timestamp> requests = MySQLFriends.getFriendRequests(uuid);
                if(requests.keySet().size() > 0) {
                    for(UUID current : requests.keySet()) {
                        String currentName = UUIDFetcher.getNameWithOfflinePlayer(current);
                        final ComponentBuilder message = new ComponentBuilder(PREFIX);

                        message.append(ChatColor.GREEN.toString() + ChatColor.BOLD + "[Accept] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept " + currentName));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to accept!")));

                        message.append(ChatColor.RED.toString() + ChatColor.BOLD + "[Decline] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends decline " + currentName));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to decline!")));

                        message.append(ChatColor.GRAY.toString() + ChatColor.BOLD + "[Ignore] ");
                        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends ignore " + currentName));
                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText("\u00a77Click to ignore")));

                        player.sendMessage(PREFIX + "");
                        player.sendMessage(PREFIX + "§lRequest from §c§l" + currentName);
                        player.spigot().sendMessage(message.create());
                    }
                } else
                    player.sendMessage(PREFIX + "§eDu hast keine Freundschafts Anfragen");

                break;

            default:
                player.sendMessage(PREFIX + "§c/friends add <Name>");
                player.sendMessage(PREFIX + "§c/friends accept <Name>");
                player.sendMessage(PREFIX + "§c/friends decline <Name>");
                player.sendMessage(PREFIX + "§c/friends ignore <Name>");
                player.sendMessage(PREFIX + "§c/friends remove <Name>");
                player.sendMessage(PREFIX + "§c/friends requests");
        }


        return false;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!MySQLFriends.hasFriendRequest(uuid)) {
            return;
        }
        HashMap<UUID, Timestamp> requests = MySQLFriends.getFriendRequests(uuid);

        final ComponentBuilder message = new ComponentBuilder(PREFIX);

        message.append(ChatColor.YELLOW + "§eDu hast " + requests.size() + " Freundschafts Anfragen");
        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends requests"));
        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText("\u00a77§aClick to see your requests")));
        player.spigot().sendMessage(message.create());
    }

}
