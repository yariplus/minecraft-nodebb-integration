package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 5/18/2016.
 */
public class ListenerPlayerChat extends ListenerNodeBBIntegration {

    public ListenerPlayerChat(NodeBBIntegrationBukkit plugin) { super(plugin); }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (SocketIOClient.disconnected()) return;
        final String socketEvent = "eventPlayerChat";

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("message", event.getMessage());
            obj.put("key", plugin.getPluginConfig().getForumAPIKey());
        } catch (JSONException e) {
            plugin.log("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        SocketIOClient.emit(socketEvent, obj, args -> {});
    }
}
