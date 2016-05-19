package com.radiofreederp.nodebbintegration.sponge.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Created by Yari on 4/11/2016.
 */
public class ListenerNodeBBIntegration {

    private static NodeBBIntegrationSponge plugin;

    public ListenerNodeBBIntegration(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationSponge) plugin;
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        MinecraftServerEvents.onPlayerJoin(plugin, event.getTargetEntity(), getPlayerJoinData(event.getTargetEntity()));
    }

    public static JSONObject getPlayerJoinData(Player player) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("key", plugin.getPluginConfig().getForumAPIKey());
            obj.put("name", player.getName());
            obj.put("id", player.getUniqueId());
        } catch (JSONException e) {
            plugin.log("Error constructing JSON Object for " + SocketIOClient.Events.onPlayerJoin);
            e.printStackTrace();
        }

        return obj;
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        String socketEvent = SocketIOClient.Events.onPlayerQuit;
        SocketIOClient.emit(socketEvent, getPlayerQuitData(event.getTargetEntity()), args -> {});
    }

    public static JSONObject getPlayerQuitData(Player player) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("key", plugin.getPluginConfig().getForumAPIKey());
            obj.put("name", player.getName());
            obj.put("id", player.getUniqueId());
        } catch (JSONException e) {
            plugin.log("Error constructing JSON Object for " + SocketIOClient.Events.onPlayerQuit);
            e.printStackTrace();
        }

        return obj;
    }
}
