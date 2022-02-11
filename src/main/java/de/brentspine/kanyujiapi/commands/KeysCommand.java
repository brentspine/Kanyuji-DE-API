package de.brentspine.kanyujiapi.commands;

import de.brentspine.kanyujiapi.mysql.data.MySQLLootChests;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KeysCommand implements CommandExecutor {

    public static final String PREFIX = "§6Keys §8» §7";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(!player.hasPermission("keys.help") || args.length <= 0) {
            UUID uuid = player.getUniqueId();
            int dices = MySQLLootChests.getDices(uuid);
            int slots = MySQLLootChests.getSlots(uuid);
            int scratchOff = MySQLLootChests.getScratchOffs(uuid);
            int universal = MySQLLootChests.getUniversal(uuid);
            player.sendMessage(PREFIX + "§2§l" + UUIDFetcher.getNameWithOfflinePlayer(uuid) + "'s key inventory");
            player.sendMessage(PREFIX + "§a - Dices: §7" + dices);
            player.sendMessage(PREFIX + "§a - Slots: §7" + slots);
            player.sendMessage(PREFIX + "§a - Scratch-Off: §7" + scratchOff);
            player.sendMessage(PREFIX + "§a - Universal: §7" + universal);
            return true;
        }
        if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            if(args.length < 3) {
                player.sendMessage(PREFIX + "§cUsage: /keys " + args[0] + " <user> <amount> <type>");
                return true;
            }

            String keyName = "";
            if(args.length >= 4) {
                keyName = args[3] + " ";
            }
            int amount;

            if(args[1].equalsIgnoreCase("@o")) {
                if(args[0].equalsIgnoreCase("add")) {
                    amount = Integer.valueOf(args[2]);
                    player.sendMessage(PREFIX + "§aYou gave " + amount + " " + keyName + "keys to everyone");
                }
                else {
                    amount = 0 - Integer.valueOf(args[2]);
                    player.sendMessage(PREFIX + "§aYou removed " + amount + " " + keyName + " keys from everyone");
                }

                if(args.length == 3) {
                    for(Player current : Bukkit.getOnlinePlayers()) {
                        MySQLLootChests.addAll(current.getUniqueId(), amount);
                    }
                }
                else {
                    switch (args[3].toLowerCase()) {
                        case "slots":
                            for(Player current : Bukkit.getOnlinePlayers()) {
                                MySQLLootChests.addSlots(current.getUniqueId(), amount);
                            }
                            break;
                        case "dices":
                            for(Player current : Bukkit.getOnlinePlayers()) {
                                MySQLLootChests.addDices(current.getUniqueId(), amount);
                            }
                            break;
                        case "scratchoff":
                            for(Player current : Bukkit.getOnlinePlayers()) {
                                MySQLLootChests.addScratchOffs(current.getUniqueId(), amount);
                            }
                            break;
                        case "universal":
                            for(Player current : Bukkit.getOnlinePlayers()) {
                                MySQLLootChests.addUniversal(current.getUniqueId(), amount);
                            }
                            break;
                        default:
                            player.sendMessage(PREFIX + "§cUnknown key type");
                            sendTypes(player);
                            break;
                    }
                }
                return true;
            }

            UUID uuid = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
            String name = UUIDFetcher.getNameWithOfflinePlayer(uuid);
            if(args[0].equalsIgnoreCase("add")) {
                amount = Integer.valueOf(args[2]);
                player.sendMessage(PREFIX + "§aYou gave§c" + amount + " " + keyName + "§akeys to " + name);
            }
            else {
                amount = 0 - Integer.valueOf(args[2]);
                player.sendMessage(PREFIX + "§aYou removed §c" + Integer.valueOf(args[2]) + " " + keyName + " §akeys from " + name);
            }

            if(args.length == 3) {
                MySQLLootChests.addAll(uuid, amount);
            }
            else {
                switch (args[3].toLowerCase()) {
                    case "slots": case "sl": case "slot":
                        MySQLLootChests.addSlots(uuid, amount);
                        break;
                    case "dices": case "d":
                        MySQLLootChests.addDices(uuid, amount);
                        break;
                    case "scratchoff": case "sc": case "so": case "scratch":
                        MySQLLootChests.addScratchOffs(uuid, amount);
                        break;
                    case "universal": case "u":
                        MySQLLootChests.addUniversal(uuid, amount);
                        break;
                    default:
                        player.sendMessage(PREFIX + "§cUnknown key type");
                        sendTypes(player);
                        break;
                }
            }
        }

        else if (args[0].equalsIgnoreCase("reset")) {
            if(args.length < 2) {
                player.sendMessage(PREFIX + "§cUsage: /keys reset <user> <type>");
                return true;
            }
            String keyName = "";
            if(args.length >= 3) {
                keyName = args[2] + " ";
            }
            if(args[1].equalsIgnoreCase("@o")) {
                player.sendMessage(PREFIX + "§aYou reset all " + keyName +  "keys for all online players");
                if(args.length == 2) {
                    for(Player current : Bukkit.getOnlinePlayers()) {
                        MySQLLootChests.reset(current.getUniqueId());
                    }
                    return true;
                }
                switch (args[2].toLowerCase()) {
                    case "slots": case "sl": case "slot":
                        for(Player current : Bukkit.getOnlinePlayers()) {
                            MySQLLootChests.setSlots(current.getUniqueId(), 0);
                        }
                        break;
                    case "dices": case "d":
                        for(Player current : Bukkit.getOnlinePlayers()) {
                            MySQLLootChests.setDices(current.getUniqueId(), 0);
                        }
                        break;
                    case "scratchoff": case "sc": case "so": case "scratch":
                        for(Player current : Bukkit.getOnlinePlayers()) {
                            MySQLLootChests.setScratchOffs(current.getUniqueId(), 0);
                        }
                        break;
                    case "universal": case "u":
                        for(Player current : Bukkit.getOnlinePlayers()) {
                            MySQLLootChests.setUniversal(current.getUniqueId(), 0);
                        }
                        break;
                    default:
                        player.sendMessage(PREFIX + "§cUnknown key type");
                        sendTypes(player);
                        break;
                }
                return true;
            }
            //keys reset brentspine <type>
            UUID uuid = UUIDFetcher.getUUIDWithOfflinePlayer(args[1]);
            String name = UUIDFetcher.getNameWithOfflinePlayer(uuid);
            player.sendMessage(PREFIX + "§aYou reset all " + keyName + "keys for " + name);
            if(args.length == 2) {
                //keys reset brentspine
                for(Player current : Bukkit.getOnlinePlayers()) {
                    MySQLLootChests.reset(current.getUniqueId());
                }
                return true;
            }
            //keys reset brentspine <type>
            switch (args[2].toLowerCase()) {
                case "slots": case "sl": case "slot":
                    MySQLLootChests.setSlots(uuid, 0);
                    break;
                case "dices": case "d":
                    MySQLLootChests.setDices(uuid, 0);
                    break;
                case "scratchoff": case "sc": case "so": case "scratch":
                    MySQLLootChests.setScratchOffs(uuid, 0);
                    break;
                case "universal": case "u":
                    MySQLLootChests.setUniversal(uuid, 0);
                    break;
                default:
                    player.sendMessage(PREFIX + "§cUnknown key type");
                    sendTypes(player);
                    break;
            }
            return true;


        }

        else if(args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
        }
        else if(args[0].equalsIgnoreCase("types")) {
            sendTypes(player);
        }
        else {
            if(args[0].equalsIgnoreCase("@o") || args[0].equalsIgnoreCase("@a")) {
                player.sendMessage(PREFIX + "§cYou can only check this for one player");
                return true;
            }
            try {
                UUID uuid = UUIDFetcher.getUUIDWithOfflinePlayer(args[0]);
                int dices = MySQLLootChests.getDices(uuid);
                int slots = MySQLLootChests.getSlots(uuid);
                int scratchOff = MySQLLootChests.getScratchOffs(uuid);
                int universal = MySQLLootChests.getUniversal(uuid);
                player.sendMessage(PREFIX + "§2§l" + UUIDFetcher.getNameWithOfflinePlayer(uuid) + "'s key inventory");
                player.sendMessage(PREFIX + "§a - Dices: §7" + dices);
                player.sendMessage(PREFIX + "§a - Slots: §7" + slots);
                player.sendMessage(PREFIX + "§a - Scratch-Off: §7" + scratchOff);
                player.sendMessage(PREFIX + "§a - Universal: §7" + universal);
            } catch (Exception e) {
                player.sendMessage(PREFIX + "§cThe player " + args[0] + " does not exist");
            }
        }
        return false;
    }


    private void sendTypes(Player player) {
        player.sendMessage(PREFIX + "§2§lThe following key types exist");
        player.sendMessage(PREFIX + "§c- slots");
        player.sendMessage(PREFIX + "§c- scratch");
        player.sendMessage(PREFIX + "§c- dices");
        player.sendMessage(PREFIX + "§c- universal");
        player.sendMessage(PREFIX + "§c- leave blank for all types");
    }

    private void sendHelp(Player player) {
        player.sendMessage(PREFIX + "- §c/keys <user>");
        player.sendMessage(PREFIX + "- §c/keys types");
        player.sendMessage(PREFIX + "- §c/keys add <user> <amount> <type>");
        player.sendMessage(PREFIX + "- §c/keys remove <user> <amount> <type>");
        player.sendMessage(PREFIX + "- §c/keys reset <user> <type>");
        player.sendMessage(PREFIX + "- §cUse '@o' to perform for online players");
        player.sendMessage(PREFIX + "- §cKeep <type> empty to apply to all types");
        player.sendMessage(PREFIX + "- §cTo apply to everyone you have to edit the database");
    }

}
