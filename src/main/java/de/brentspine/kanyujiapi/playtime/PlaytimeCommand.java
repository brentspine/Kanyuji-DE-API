package de.brentspine.kanyujiapi.playtime;

import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.MySQLPlaytime;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {

    //permissions
        //See own - playtime.own

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Playtime.PREFIX + "Please use this command inGame");
            return false;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if(args.length == 0) {
            if(player.hasPermission("playtime.own")) {
                player.sendMessage(Playtime.PREFIX + "Your Playtime: " + MySQLPlaytime.getFormattedTime(uuid));
                return true;
            }
            player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
            return true;
        }
        switch (args[0]) {
            case "help":
                if(!player.hasPermission("playtime.help")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                player.sendMessage(Playtime.PREFIX + "- §c/playtime <user>");
                player.sendMessage(Playtime.PREFIX + "- §c/playtime top");
                player.sendMessage(Playtime.PREFIX + "- §c/playtime add <user> <amount>");
                player.sendMessage(Playtime.PREFIX + "- §c/playtime remove <user> <amount>");
                player.sendMessage(Playtime.PREFIX + "- §c/playtime reset <user>");
                break;
            case "top":
                if(!player.hasPermission("playtime.top")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                String message = Playtime.PREFIX + "§2§lPlayers with the most Playtime:";
                HashMap<Integer, String> topList = MySQLPlaytime.getTopPlayers(10);
                for (int i = 0; i < topList.size(); i++) {
                    message = message + "\n" + Playtime.PREFIX + "§c" + UUIDFetcher.getUUID(topList.get(i)) + ": §7" + MySQLPlaytime.getFormattedTime(UUID.fromString(topList.get(i)));
                }
                player.sendMessage(message);
                break;
            case "set":
                if(!player.hasPermission("playtime.set")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                if(args.length < 3) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime set <Player> <Amount>");
                    break;
                }
                try {
                    Integer.valueOf(args[2]);
                } catch (NumberFormatException | NullPointerException e) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime set <Player> <amount>");
                    break;
                }
                MySQLPlaytime.set(UUIDFetcher.getUUID(args[1]), Integer.valueOf(args[2]));
                player.sendMessage(Playtime.PREFIX + "You set §c" + MySQLPlaytime.formatTime(Integer.valueOf(args[2])) + "§7 for §c" + args[1]);
                break;
            case "add":
                if(!player.hasPermission("playtime.add")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                if(args.length < 3) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime add <Player> <Amount>");
                    break;
                }
                try {
                    Integer.valueOf(args[2]);
                } catch (NumberFormatException | NullPointerException e) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime add <Player> <amount>");
                    break;
                }
                MySQLPlaytime.addMinutesPlayed(UUIDFetcher.getUUID(args[1]), Integer.valueOf(args[2]));
                player.sendMessage(Playtime.PREFIX + "You added §c" + MySQLPlaytime.formatTime(Integer.valueOf(args[2])) + "§7 for §c" + args[1]);
                break;
            case "remove":
                if(!player.hasPermission("playtime.remove")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                if(args.length < 3) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime remove <Player> <Amount>");
                    break;
                }
                try {
                    Integer.valueOf(args[2]);
                } catch (NumberFormatException | NullPointerException e) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime remove <Player> <amount>");
                    break;
                }
                MySQLPlaytime.removeMinutesPlayed(UUIDFetcher.getUUID(args[1]), Integer.valueOf(args[2]));
                player.sendMessage(Playtime.PREFIX + "You removed §c" + MySQLPlaytime.formatTime(Integer.valueOf(args[2])) + "§7 for §c" + args[1]);
                break;
            case "reset":
                if(!player.hasPermission("playtime.reset")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                if(args.length < 2) {
                    player.sendMessage(Playtime.PREFIX + "§cUsage: /playtime reset <player>");
                    return true;
                }
                MySQLPlaytime.reset(UUIDFetcher.getUUID(args[1]));
                player.sendMessage(Playtime.PREFIX + "The playtime for §c" + args[1] + "§7 was resetted");
                break;
            default:
                if(!player.hasPermission("playtime.other")) {
                    player.sendMessage(KanyujiAPI.getNoPerm(Playtime.PREFIX));
                    return true;
                }
                player.sendMessage(Playtime.PREFIX + "§c" + args[0] + "'s playtime: §7" + MySQLPlaytime.getFormattedTime( UUIDFetcher.getUUID(args[0]) ));
                break;
        }
        return false;
    }

}
