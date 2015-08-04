package com.yaricraft.nodebbintegration;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URISyntaxException;

public class SocketIOClient extends BukkitRunnable {

    private static SocketIOClient instance;

    private JavaPlugin plugin;
    private IO.Options options;
    private static Socket socket;

    private SocketIOClient(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static SocketIOClient create(JavaPlugin plugin) {
        if (instance == null) instance = new SocketIOClient(plugin);
        return instance;
    }

    public static Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            try {
                socket = IO.socket(plugin.getConfig().getString("FORUMURL"));
                System.out.println("Socket created.");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Oops, something went wrong with the socket server. I tried to start a duplicate instance.");
        }

        if (socket != null) {
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected to forum.");
                }
            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    // Placeholder
                    System.out.println("EVENT_EVENT");
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_DISCONNECT");
                }

            });

            socket.connect();
        }else{
            System.out.println("Socket connection failed.");
        }
    }
}
