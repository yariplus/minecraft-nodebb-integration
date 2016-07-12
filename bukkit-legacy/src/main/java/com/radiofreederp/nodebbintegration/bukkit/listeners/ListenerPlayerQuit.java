package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import io.socket.client.Ack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 5/18/2016.
 */
public class ListenerPlayerQuit implements Listener {

    private NodeBBIntegrationBukkit plugin;

    public ListenerPlayerQuit(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String socketEvent = SocketIOClient.Events.onPlayerQuit;
        SocketIOClient.emit(socketEvent, getPlayerQuitData(event.getPlayer()), new Ack() {
            @Override
            public void call(Object... args) {
            }
        });
    }

    public static JSONObject getPlayerQuitData(Player player) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", player.getName());
            obj.put("id", player.getUniqueId());
            obj.put("key", NodeBBIntegrationBukkit.instance.getPluginConfig().getForumAPIKey());
        } catch (JSONException e) {
            NodeBBIntegrationBukkit.instance.log("Error constructing JSON Object for " + SocketIOClient.Events.onPlayerQuit);
            e.printStackTrace();
        }

        return obj;
    }
}
