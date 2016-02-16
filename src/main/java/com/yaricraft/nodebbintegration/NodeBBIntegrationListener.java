package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
import com.yaricraft.nodebbintegration.hooks.OnTimeHook;
import com.yaricraft.nodebbintegration.hooks.VanishNoPacketHook;
import com.yaricraft.nodebbintegration.hooks.VaultHook;
import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Yari on 5/28/2015.
 */
public class NodeBBIntegrationListener implements Listener {

    private JavaPlugin plugin;

    public NodeBBIntegrationListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private String getNamespace() {
        String ns = plugin.getConfig().getString("SOCKETNAMESPACE");
        String pl = plugin.getConfig().getString("PLUGINID");

        return ns + "." + pl + ".";
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        SocketIOClient.sendPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        SocketIOClient.sendPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        if (SocketIOClient.getSocket() == null) return;
        final String socketEvent = getNamespace() + "eventPlayerChat";

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("message", event.getMessage());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            NodeBBIntegration.log("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        NodeBBIntegration.log("Sending " + socketEvent);
        SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
            @Override
            public void call(Object... args) {
                NodeBBIntegration.log(socketEvent + " callback received.");
            }
        });
    }

    // TEMP
    @EventHandler(priority= EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            String format = "";
            if (VaultHook.chat != null && VaultHook.permission != null) {
                String[] groups = VaultHook.permission.getPlayerGroups(null, player);
                World world = Bukkit.getWorlds().get(0);

                for (int i = 0; i < groups.length; i++) {
                    format = format + ChatColor.translateAlternateColorCodes('&', VaultHook.chat.getGroupPrefix(world, groups[i]));
                }
                format = format + "%1$s" + ChatColor.RESET + ": %2$s";
                event.setFormat(format);
            }
        }
    }

    // TODO
    @EventHandler
    public void onServerListPing(final ServerListPingEvent event) {
        if (SocketIOClient.getSocket() == null) return;

        NodeBBIntegration.log("Server List Ping from: " + event.getAddress().toString());
    }

    // TODO: Why does this throw errors?
    @EventHandler
    public void onWorldSave(WorldSaveEvent event)
    {
        PlayerManager.saveConfig();
        NodeBBIntegration.log("Saved player data.");
    }
}
