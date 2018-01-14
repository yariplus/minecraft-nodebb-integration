package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.bukkit.configuration.PluginConfigBukkit;
import com.radiofreederp.nodebbintegration.listeners.ListenerNodeBBIntegration;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.utils.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONException;
import org.json.JSONObject;

public class ListenerPlayerChat extends ListenerNodeBBIntegration implements Listener {

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
            obj.put("key", PluginConfigBukkit.instance.getForumAPIKey());
        } catch (JSONException e) {
            Logger.log("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        SocketIOClient.emit(socketEvent, obj, args -> {});
    }
}
