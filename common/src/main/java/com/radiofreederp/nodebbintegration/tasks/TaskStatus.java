package com.radiofreederp.nodebbintegration.tasks;

import com.radiofreederp.nodebbintegration.MinecraftServerCommon;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.socketio.ESocketEvent;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.utils.Logger;
import io.socket.client.Ack;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskStatus implements Runnable {

    private final MinecraftServerCommon server;
    private static TaskStatus instance;

    public TaskStatus(NodeBBIntegrationPlugin plugin){
        instance = this;
        this.server = plugin.getMinecraftServer();

        plugin.runTaskTimer(instance, 20 * 5, 20 * 60);
    }

    public static TaskStatus getTask () {
        return instance;
    }

    @Override
    public void run() {
        if (SocketIOClient.disconnected()) {
            SocketIOClient.connect();
            return;
        }

        final String socketEvent = ESocketEvent.SERVER_STATUS;

        Logger.log("Constructing JSON Object for " + socketEvent + "...");

        JSONObject obj = new JSONObject();

        try {
            obj.put("timestamp", System.currentTimeMillis());

            obj.put("tps", server.getTPS());
            obj.put("version", server.getVersion());
            obj.put("name", server.getServerName());
            obj.put("gametype", server.getWorldType());
            obj.put("map", server.getWorldName());
            obj.put("motd", server.getMotd());

            obj.put("players", server.getPlayerList());
            obj.put("onlinePlayers", server.getOnlinePlayers());
            obj.put("maxPlayers", server.getMaxPlayers());

            obj.put("plugins", server.getPluginList());

            obj.put("icon", server.getServerIcon());

            obj.put("objectives", server.getScoreboards());

        } catch (Exception e) {
            Logger.log("Error constructing JSON Object for " + socketEvent);
            e.printStackTrace();
            return;
        }

        SocketIOClient.emit(socketEvent, obj, null);
    }
}
