package com.yaricraft.nodebbintegration;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SocketServer extends BukkitRunnable {

    private static SocketServer instance;

    private JavaPlugin plugin;
    private Configuration config;
    private SocketIOServer server;

    private SocketServer(JavaPlugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.config = new Configuration();
        this.config.setHostname("localhost");
        this.config.setPort(25665);
    }

    public static SocketServer create(JavaPlugin plugin) {
        if (instance == null) instance = new SocketServer(plugin);
        return instance;
    }

    public static SocketServer getInstance() {
        return instance;
    }

    @Override
    public void run() {
        server = new SocketIOServer(config);
        server.addListeners(this);
        server.start();
    }

    @OnEvent("someevent")
    public void onSomeEventHandler(SocketIOClient client, Object data, AckRequest ackRequest) {
        System.out.println("Received someevent");
    }

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
        System.out.println("Connected to forum");
    }

    @OnDisconnect
    public void onDisconnectHandler(SocketIOClient client) {
        System.out.println("Disconnected");
    }

    public void emitPlayerJoin(String data) {
        server.getBroadcastOperations().sendEvent("PlayerJoin", data);
    }

    public void emitPlayerQuit(String data) {
        server.getBroadcastOperations().sendEvent("PlayerQuit", data);
    }

    public void emitPlayerChat(String data) { server.getBroadcastOperations().sendEvent("PlayerChat", data); }
}
