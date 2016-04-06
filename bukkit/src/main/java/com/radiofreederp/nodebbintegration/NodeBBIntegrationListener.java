package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import io.socket.client.Ack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Yari on 5/28/2015.
 */
public class NodeBBIntegrationListener implements Listener {

    private JavaPlugin plugin;

    public NodeBBIntegrationListener(JavaPlugin plugin) {
        this.plugin = plugin;
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
        if (SocketIOClient.disconnected()) return;
        final String socketEvent = "eventPlayerChat";

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("message", event.getMessage());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            NodeBBIntegrationBukkit.log("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        NodeBBIntegrationBukkit.log("Sending " + socketEvent);
        SocketIOClient.emit(socketEvent, obj, new Ack() {
            @Override
            public void call(Object... args) {
                NodeBBIntegrationBukkit.log(socketEvent + " callback received.");
            }
        });
    }

    // TODO
    @EventHandler
    public void onServerListPing(final ServerListPingEvent event) {
        if (SocketIOClient.disconnected()) return;

        NodeBBIntegrationBukkit.log("Server List Ping from: " + event.getAddress().toString());
    }

    // Allow the WorldSaveEvent to save the config once a minute.
    private long stamp = 0;
    @EventHandler
    public void onWorldSave(WorldSaveEvent event)
    {
        long now = new Date().getTime();
        if (now - stamp > 60000) {
            stamp = now;
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerManager.saveConfig();
                    NodeBBIntegrationBukkit.log("Saved player data.");
                }
            }.runTask(plugin);
        }
    }
}
