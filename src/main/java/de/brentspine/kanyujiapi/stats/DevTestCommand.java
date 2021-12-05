package de.brentspine.kanyujiapi.stats;

import de.brentspine.kanyujiapi.mysql.stats.MySQLSurf;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(Integer.toString(MySQLSurf.getPoints(player.getUniqueId())));
        return false;
    }

}
