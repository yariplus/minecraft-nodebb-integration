package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 4/21/2016.
 */
public interface MinecraftServerEvents {

    static void onPlayerJoin(NodeBBIntegrationPlugin plugin, Object player, JSONObject data) {
        String socketEvent = SocketIOClient.Events.onPlayerJoin;

        SocketIOClient.emit(socketEvent, data, args -> {

            // We receive feedback form the forum about the user.
            // First arg is an error object.
            if (args[0] == null) {

                // Construct response object.
                if (args[1] != null) {

                    JSONObject resObj = ((JSONObject)args[1]);
                    JSONObject user = null;

                    try {
                        user = resObj.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (user != null) {
                        plugin.getMinecraftServer().sendMessage(player, "Thanks for registering at " + plugin.getPluginConfig().getForumName());
                    }else{
                        plugin.getMinecraftServer().sendMessage(player, "Use /register to create an account at " + plugin.getPluginConfig().getForumURL());
                    }
                }else{
                    plugin.log(socketEvent + " callback no response object found.");
                }
            }else{
                plugin.log(socketEvent + " callback error.");
            }
        });
    }
}
