package com.yaricraft.nodebbintegration;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIOClient extends BukkitRunnable {

    private static SocketIOClient instance;

    private static JavaPlugin plugin;
    private static Socket socket;
    public static String id;

    public static String getNamespace() {
        String ns = plugin.getConfig().getString("SOCKETNAMESPACE");
        String pl = plugin.getConfig().getString("PLUGINID");

        return ns + "." + pl + ".";
    }

    private SocketIOClient(JavaPlugin _plugin) {
        plugin = _plugin;
    }

    public static SocketIOClient create(JavaPlugin plugin) {
        if (instance == null) instance = new SocketIOClient(plugin);
        return instance;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (socket == null) {
            try {
                socket = IO.socket(plugin.getConfig().getString("FORUMURL"));
                NodeBBIntegration.log("Success! The socket client was created.");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            NodeBBIntegration.log("Oops, something went wrong with the socket client. I tried to start a duplicate instance.");
        }

        if (socket != null) {
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    sync();
                }
            }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on("eventWebChat", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    // Interpret message.
                    if (args[0] != null) {
                        try {
                            final String name = ((JSONObject)args[0]).getString("name");
                            final String message = ((JSONObject)args[0]).getString("message");
                            Bukkit.broadcastMessage("[" + ChatColor.GOLD + "WEB" + ChatColor.RESET + "] <" + name + "> " + message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Oh no! I lost my connection to the forum. I'll try to re-connect later.");
                }
            });

            socket.connect();
        }else{
            System.out.println("Uh Oh. I failed to create a socket client. Are you sure the forum url is correct?");
        }
    }

    // Socket events are sent asynchronously.
    public static void send(final String event, final JSONObject object) {
        SocketIOClient.instance._send(event, object);
    }

    private void _send(final String event, final JSONObject object) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(socket.connected()) socket.emit(event, object);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void sync() {
        NodeBBIntegration.log("Hurray! I connected to the forum!");
        id = socket.id();
        ((NodeBBIntegration)plugin).taskTick.run();
    }
}
