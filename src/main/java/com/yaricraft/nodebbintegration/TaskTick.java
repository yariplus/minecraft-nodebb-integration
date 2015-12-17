package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Yari on 6/12/2015.
 */
public class TaskTick extends BukkitRunnable {

    private static TaskTick instance;

    private static NodeBBIntegration plugin;

    private long timeLast;
    private String TPS;

    public TaskTick(NodeBBIntegration _plugin){
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

        if (SocketIOClient.getSocket().connected()) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    final String socketEvent = SocketIOClient.getNamespace() + "eventStatus";

                    JSONObject obj = new JSONObject();
                    final ArrayList<JSONObject> players = new ArrayList<JSONObject>();
                    final ArrayList<JSONObject> pluginList = new ArrayList<JSONObject>();

                    try {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
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
                        obj.put("key", plugin.getConfig().getString("APIKEY"));
                        obj.put("players", players);

                        obj.put("version", Bukkit.getVersion());
                        obj.put("name", Bukkit.getServer().getServerName());

                        obj.put("gametype", Bukkit.getServer().getWorldType());
                        obj.put("map", Bukkit.getWorlds().get(0).getName());

                        obj.put("motd", Bukkit.getServer().getMotd());
                        obj.put("onlinePlayers", Bukkit.getOnlinePlayers().size());
                        obj.put("maxPlayers", Bukkit.getMaxPlayers());
                        obj.put("pluginList", pluginList);

                        File file = new File("server-icon.png");

                        if (file.isFile()) {
                            try {
                                obj.put("icon", "data:image/png;base64," + Base64.encodeBase64String(FileUtils.readFileToByteArray(file)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        NodeBBIntegration.log("Error constructing JSON Object for " + socketEvent);
                        e.printStackTrace();
                        return;
                    }

                    NodeBBIntegration.log("Sending " + socketEvent);
                    SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
                        @Override
                        public void call(Object... args) {
                            NodeBBIntegration.log(socketEvent + " callback received.");
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
