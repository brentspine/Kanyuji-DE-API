package de.brentspine.kanyujiapi.util;

import de.brentspine.kanyujiapi.KanyujiAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private KanyujiAPI plugin;
    public FileConfiguration rankConfig;

    public ConfigManager(KanyujiAPI plugin, File dataFolder) {
        this.plugin = plugin;
        File rankFile = new File(dataFolder, "ranks.yml");
        if(!rankFile.exists())
            plugin.saveResource("ranks.yml", false);
        this.rankConfig = YamlConfiguration.loadConfiguration(rankFile);

    }

}
