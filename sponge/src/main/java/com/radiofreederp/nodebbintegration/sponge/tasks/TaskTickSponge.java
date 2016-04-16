package com.radiofreederp.nodebbintegration.sponge.tasks;

import com.google.common.io.Files;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by Yari on 4/10/2016.
 */
public class TaskTickSponge implements Runnable{

    private static TaskTickSponge instance;

    private static NodeBBIntegrationSponge plugin;

    private long timeLast;
    private String TPS;

    public TaskTickSponge(NodeBBIntegrationSponge _plugin){
        plugin = _plugin;
        timeLast = System.currentTimeMillis();
        instance = this;
        plugin.runTaskTimerAsynchronously(instance);
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
            plugin.runTask(() -> {

                    final String socketEvent = "eventStatus";

                    JSONObject obj = new JSONObject();
                    final ArrayList<JSONObject> players = new ArrayList<JSONObject>();
                    final ArrayList<JSONObject> pluginList = new ArrayList<JSONObject>();

                    try {
                        for (Player player : Sponge.getServer().getOnlinePlayers()) {
                            JSONObject _player = new JSONObject();
                            try {
                                _player.put("name", player.getName());
                                _player.put("id", player.getUniqueId());

                                players.add(_player);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (PluginContainer plugin : Sponge.getPluginManager().getPlugins()) {
                            JSONObject _plugin = new JSONObject();
                            try {
                                _plugin.put("name", plugin.getName());
                                _plugin.put("version", plugin.getVersion());
                                pluginList.add(_plugin);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        obj.put("tps", TaskTickSponge.getTPS());
                        obj.put("key", plugin.getPluginConfig().getForumAPIKey());
                        obj.put("players", players);

                        obj.put("version", Sponge.getPlatform().getMinecraftVersion().getName());
                        // TODO
                        obj.put("name", "name");

                        // TODO
                        //obj.put("gametype", Sponge.getServer());
                        obj.put("map", Sponge.getServer().getDefaultWorld().get().getWorldName());

                        obj.put("motd", Sponge.getServer().getMotd());
                        obj.put("onlinePlayers", Sponge.getServer().getOnlinePlayers().size());
                        obj.put("maxPlayers", Sponge.getServer().getMaxPlayers());
                        obj.put("pluginList", pluginList);

                        // Server Icon
                        try {
                            File file = new File("server-icon.png");
                            if (file.isFile()) {
                                String icon = Base64.getEncoder().encodeToString(Files.toByteArray(file));
                                obj.put("icon", "data:image/png;base64," + icon);
                            }
                        } catch (IOException |JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        plugin.log("Error constructing JSON Object for " + socketEvent);
                        e.printStackTrace();
                        return;
                    }

                    plugin.log("Sending " + socketEvent);
                    SocketIOClient.emit(socketEvent, obj, args -> plugin.log(socketEvent + " callback received."));
                }
            );
        }
    }

    public static String getTPS() {
        return instance.TPS;
    }
}
