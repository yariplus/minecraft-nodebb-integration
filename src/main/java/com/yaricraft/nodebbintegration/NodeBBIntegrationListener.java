package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
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

        JSONObject obj = new JSONObject();

        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Sending " + getNamespace() + "eventPlayerJoin");

        SocketIOClient.getSocket().emit(getNamespace() + "eventPlayerJoin", obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("PlayerJoinEvent received.");
            }
        });
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (SocketIOClient.getSocket() == null) return;

        JSONObject obj = new JSONObject();

        try {
            obj.put("name", event.getPlayer().getName());
            obj.put("id", event.getPlayer().getUniqueId());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Sending " + getNamespace() + "eventPlayerQuit");

        SocketIOClient.getSocket().emit(getNamespace() + "eventPlayerQuit", obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("PlayerQuitEvent received.");
            }
        });
    }

    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        if (SocketIOClient.getSocket() == null) return;

        JSONObject obj = new JSONObject();

        try {
            obj.put("player", event.getPlayer().getName());
            obj.put("message", event.getMessage());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Sending " + getNamespace() + "eventPlayerChat");

        SocketIOClient.getSocket().emit(getNamespace() + "eventPlayerChat", obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("PlayerChatEvent received.");
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

        JSONObject obj = new JSONObject();

        try {
            obj.put("tps", TaskTick.getTPS());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Sending " + getNamespace() + "emitTPS");

        SocketIOClient.getSocket().emit(getNamespace() + "emitTPS", obj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("emitTPS received.");
            }
        });
    }
}
