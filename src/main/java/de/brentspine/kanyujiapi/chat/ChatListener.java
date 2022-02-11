package de.brentspine.kanyujiapi.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.brentspine.kanyujiapi.KanyujiAPI;
import de.brentspine.kanyujiapi.mysql.data.MySQLChat;
import de.brentspine.kanyujiapi.util.UUIDFetcher;
import de.dytanic.cloudnet.driver.permission.CachedPermissionManagement;
import de.dytanic.cloudnet.driver.permission.PermissionUserGroupInfo;
import de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsHelper;
import de.dytanic.cloudnet.ext.cloudperms.node.CloudNetCloudPermissionsModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Type;
import java.util.UUID;

public class ChatListener implements Listener {

    private KanyujiAPI plugin;
    //private ProtocolManager manager;
    private CachedPermissionManagement cachedPermissionManagement;
    private boolean running;

    public ChatListener(KanyujiAPI plugin) {
        this.plugin = plugin;
        //this.manager = ProtocolLibrary.getProtocolManager();
        this.cachedPermissionManagement = CloudPermissionsHelper.getCachedPermissionManagement();
    }

    private void run() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.getMinecraftVersion().getVersion();
        /*manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                //if (isMuted(player)) {
                    System.out.println("[MUTED] " + player.getName() + ": " + packet.getStrings().read(0));
                    event.setCancelled(true);
                //}
            }
        });*/
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                String message = packet.getStrings().read(0);
                if(message.startsWith("/")) {
                    return;
                }
                event.setCancelled(true);
                PermissionUserGroupInfo rank = getRank(player);
                String prefix = getRankPrefix(rank);
                for(Player current : Bukkit.getOnlinePlayers()) {
                    if(MySQLChat.hasUserIgnored(current.getUniqueId(), player.getUniqueId()))
                        current.sendMessage(prefix + player.getName() + " » <Blocked Message>");
                    else
                        current.sendMessage(prefix + player.getName() + " » " + message);
                }
            }
        });
    }

    //Waiting to run the program until a player joins to prevent bugs that happen when ProtocolLib is not finished loading
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!running) {
            running = true;
            run();
        }
    }

    public PermissionUserGroupInfo getRank(OfflinePlayer player) {
        PermissionUserGroupInfo rank = new PermissionUserGroupInfo("Player", -1);
        for(PermissionUserGroupInfo groupInfo : cachedPermissionManagement.getUser(player.getUniqueId()).getGroups()) {
            rank = groupInfo;
        }
        return rank;
    }

    public String getRankPrefix(PermissionUserGroupInfo rank) {
        return ChatColor.translateAlternateColorCodes('&', KanyujiAPI.getConfigManager().rankConfig.getString("ranks." + rank.getGroup().toLowerCase() + ".prefix"));
    }

    public String getRankColor(PermissionUserGroupInfo rank) {
        return ChatColor.translateAlternateColorCodes('&', KanyujiAPI.getConfigManager().rankConfig.getString("ranks." + rank.getGroup().toLowerCase() + ".color"));
    }

    public String getRankPrefix(OfflinePlayer player) {
        PermissionUserGroupInfo rank = getRank(player);
        return ChatColor.translateAlternateColorCodes('&', KanyujiAPI.getConfigManager().rankConfig.getString("ranks." + rank.getGroup().toLowerCase() + ".prefix"));
    }

    public String getRankColor(OfflinePlayer player) {
        PermissionUserGroupInfo rank = getRank(player);
        return ChatColor.translateAlternateColorCodes('&', KanyujiAPI.getConfigManager().rankConfig.getString("ranks." + rank.getGroup().toLowerCase() + ".color"));
    }


}
