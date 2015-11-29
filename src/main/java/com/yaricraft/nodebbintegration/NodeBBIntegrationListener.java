package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
import me.edge209.OnTime.OnTimeAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

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
        if (SocketIOClient.getSocket() == null) return;
        final String socketEvent = getNamespace() + "eventPlayerJoin";

        Player player = event.getPlayer();

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", player.getName());
            obj.put("id", player.getUniqueId());
            obj.put("key", plugin.getConfig().getString("APIKEY"));

            if (NodeBBIntegration.ontime && OnTimeAPI.playerHasOnTimeRecord(player.getName())) {
                obj.put("playtime", OnTimeAPI.getPlayerTimeData(player.getName(), OnTimeAPI.data.TOTALPLAY));
            }

            if (NodeBBIntegration.chat != null && NodeBBIntegration.permission != null) {
                String prefix = NodeBBIntegration.chat.getPlayerPrefix(player);
                if (prefix == null) prefix = NodeBBIntegration.chat.getGroupPrefix(Bukkit.getWorlds().get(0), NodeBBIntegration.permission.getPrimaryGroup(player));
                obj.put("prefix", prefix);
            }
        } catch (JSONException e) {
            System.out.println("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        System.out.println("Sending " + socketEvent);
        SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println(socketEvent + " callback received.");
            }
        });
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (SocketIOClient.getSocket() == null) return;
        final String socketEvent = getNamespace() + "eventPlayerQuit";

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            System.out.println("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        System.out.println("Sending " + socketEvent);
        SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println(socketEvent + " callback received.");
            }
        });
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
            System.out.println("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        System.out.println("Sending " + socketEvent);
        SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println(socketEvent + " callback received.");
            }
        });
    }

    @EventHandler
    public void handleServerCommand(ServerCommandEvent event) {
        // Placeholder
        //String data = event.getSender().getName() + ":" + event.getCommand();
        //SocketIOClient.getInstance().emitServerCommand(data);
    }

    @EventHandler
    public void onServerListPing(final ServerListPingEvent event) {
        if (SocketIOClient.getSocket() == null) return;

        System.out.println("Server list ping from: " + event.getAddress().toString());

    }
}
