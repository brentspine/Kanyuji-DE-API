package de.brentspine.kanyujiapi.commands;

import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.data.MySQLChat;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

public class IgnoreCommand implements CommandExecutor {

    public static final String PREFIX = "§eIgnore §8» §7";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(args.length <= 0) {
            sendHelp(player);
            return true;
        }


        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "help":
                sendHelp(player);
                break;
            case "remove":
                if(!MySQLChat.hasUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[1]))) {
                    player.sendMessage(PREFIX + "§cThis player is not ignored");
                    return true;
                }
                MySQLChat.removeUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[1]));
                player.sendMessage(PREFIX + "§aYou removed " + args[1] + " from your ignored list");
                break;
            case "list":
                String message = "§2§lIgnored Players";
                for(UUID ignored : MySQLChat.getAllUsersIgnored(player.getUniqueId())) {
                    message += "\n" + KanyujiAPI.getChatListener().getRankColor(Bukkit.getOfflinePlayer(ignored)) + UUIDFetcher.getNameWithOfflinePlayer(ignored) + " - §7since " + MySQLChat.getIgnoredSince(player.getUniqueId(), ignored);
                }
                player.sendMessage(message);
                break;
            case "add":
                if(MySQLChat.hasUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[1]))) {
                    player.sendMessage(PREFIX + "§cThis player is already ignored");
                    return true;
                }
                if(args[1].equalsIgnoreCase(player.getName())) {
                    player.sendMessage(PREFIX + "§cYou can't ignore yourself");
                    return true;
                }
                MySQLChat.addUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[1]));
                player.sendMessage(PREFIX + "§aYou are now ignoring " + args[1]);
                break;
            default:
                if(MySQLChat.hasUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[0]))) {
                    player.sendMessage(PREFIX + "§cThis player is already ignored");
                    return true;
                }
                if(args[0].equalsIgnoreCase(player.getName())) {
                    player.sendMessage(PREFIX + "§cYou can't ignore yourself");
                    return true;
                }
                MySQLChat.addUserIgnored(player.getUniqueId(), UUIDFetcher.getUUIDWithOfflinePlayer(args[0]));
                player.sendMessage(PREFIX + "§aYou are now ignoring " + args[0]);
                break;
        }
        return false;
    }

    private void sendHelp(Player player) {
        player.sendMessage(PREFIX + "§c/ignore <player>");
        player.sendMessage(PREFIX + "§c/ignore add <player>");
        player.sendMessage(PREFIX + "§c/ignore remove <player>");
        player.sendMessage(PREFIX + "§c/ignore list");
        player.sendMessage(PREFIX + "§c/ignore help");
    }

}
