package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.utils.Doable;
import com.radiofreederp.nodebbintegration.utils.Logger;
import io.socket.client.Ack;
import org.json.JSONException;
import org.json.JSONObject;

public class MinecraftServerEvents {

    public static NodeBBIntegrationPlugin plugin;

    private static boolean applyKey(JSONObject object) {
        try {
            object.put("key", PluginConfig.instance.getForumAPIKey());
        } catch (JSONException e) {
            //TODO
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }

    private static void sendEvent(final String socketEvent, final JSONObject data, final Doable res) {

        // Send socket event to the forum.
        SocketIOClient.emit(socketEvent, data, new Ack() {
            @Override
            public void call(Object... args) {

                // We receive a callback form the forum with information about the primary linked user.
                // First arg is an error object.
                if (args[0] == null) {

                    // Construct response object.
                    if (args[1] != null) {

                        // Call the callback if it exists.
                        if (res != null) res.doit(args[1]);
                    } else {
                        Logger.log(socketEvent + " callback no response object found.");
                    }
                } else {
                    Logger.log(socketEvent + " callback error.");
                    try {
                        Logger.log(((JSONObject) args[0]).getString("message"));
                    } catch (JSONException e) {
                        //TODO
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void onPlayerJoin(final Object player, final JSONObject data) {
        final String socketEvent = SocketIOClient.Events.onPlayerJoin;

        if (!applyKey(data)) return;

        sendEvent(socketEvent, data, new Doable() {
            @Override
            public void doit(Object object) {
                JSONObject resObj = ((JSONObject) object);
                JSONObject user = null;

                if (resObj.has("user") && !resObj.isNull("user")) {
                    try {
                        user = resObj.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Inform the player.
                if (user != null) {
                    plugin.getMinecraftServer().sendMessage(player, "Thanks for registering at " + PluginConfig.instance.getForumName());
                } else {
                    plugin.getMinecraftServer().sendMessage(player, "Use /register to create an account at " + PluginConfig.instance.getForumURL());
                    //TODO: This is where we could add forced registration, or pushy limitations until registered.
                }
            }
        });
    }

    public static void onPlayerQuit(final Object player, final JSONObject data) {
        final String socketEvent = SocketIOClient.Events.onPlayerQuit;

        if (!applyKey(data)) return;

        sendEvent(socketEvent, data, null);
    }
}
