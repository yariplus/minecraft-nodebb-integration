package com.radiofreederp.nodebbintegration.tasks;

import com.google.common.io.Files;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import io.socket.client.Ack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by Yari on 6/12/2015.
 */
public class TaskTick extends BukkitRunnable {

    private static TaskTick instance;

    private static NodeBBIntegrationBukkit plugin;

    private long timeLast;
    private String TPS;

    public TaskTick(NodeBBIntegrationBukkit _plugin){
        plugin = _plugin;
        if (instance != null) instance.cancel();
        timeLast = System.currentTimeMillis();
        instance = this;
        instance.runTaskTimerAsynchronously(plugin, 20 * 60, 20 * 60);
    }

    @Override
    public void run() {
        long timeNow = System.currentTimeMillis();
        long diff = ( timeNow - timeLast );
        double ticks = 20D / ( ((double)diff) / 60000D );
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        TPS = df.format(ticks);
        timeLast = timeNow;

        if (SocketIOClient.connected()) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    final String socketEvent = "eventStatus";

                    JSONObject obj = new JSONObject();
                    final ArrayList<JSONObject> players = new ArrayList<JSONObject>();
                    final ArrayList<JSONObject> pluginList = new ArrayList<JSONObject>();

                    try {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            if (VanishNoPacketHook.isEnabled()) {
                                if (VanishNoPacketHook.isVanished(player.getName())) continue;
                            }
                            JSONObject _player = new JSONObject();
                            try {
                                _player.put("name", player.getName());
                                _player.put("id", player.getUniqueId());

                                players.add(_player);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
                            JSONObject _plugin = new JSONObject();
                            try {
                                _plugin.put("name", plugin.getName());
                                _plugin.put("version", plugin.getDescription().getVersion());
                                pluginList.add(_plugin);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        obj.put("tps", TaskTick.getTPS());
                        obj.put("key", plugin.getPluginConfig().getForumAPIKey());
                        obj.put("players", players);

                        obj.put("version", Bukkit.getVersion());
                        obj.put("name", Bukkit.getServer().getServerName());

                        obj.put("gametype", Bukkit.getServer().getWorldType());
                        obj.put("map", Bukkit.getWorlds().get(0).getName());

                        obj.put("motd", Bukkit.getServer().getMotd());
                        obj.put("onlinePlayers", Bukkit.getOnlinePlayers().size());
                        obj.put("maxPlayers", Bukkit.getMaxPlayers());
                        obj.put("pluginList", pluginList);

                        // Server Icon
                        try {
                            File file = new File("server-icon.png");
                            if (file.isFile()) {
                                String icon = Base64.getEncoder().encodeToString(Files.toByteArray(file));
                                obj.put("icon", "data:image/png;base64," + icon);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        plugin.log("Error constructing JSON Object for " + socketEvent);
                        e.printStackTrace();
                        return;
                    }

                    plugin.log("Sending " + socketEvent);
                    SocketIOClient.emit(socketEvent, obj, new Ack() {
                        @Override
                        public void call(Object... args) {
                            plugin.log(socketEvent + " callback received.");
                        }
                    });
                }
            }.runTask(plugin);
        }
    }

    public static String getTPS() {
        return instance.TPS;
    }
}
