package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 4/21/2016.
 */
public interface MinecraftServerEvents {

    static void onPlayerJoin(NodeBBIntegrationPlugin plugin, JSONObject data) {
        String socketEvent = SocketIOClient.Events.onPlayerJoin;

        SocketIOClient.emit(socketEvent, data, args -> {

            // We receive feedback form the forum about the user.
            // First arg is an error object.
            if (args[0] == null) {

                // Construct response object.
                JSONObject resObj = ((JSONObject)args[1]);
                if (resObj != null) {

                    try {
                        if (resObj.getBoolean("isRegistered")) {
                            // TODO: Send notifications.
                        }else{
                            // TODO: Message with instructions on how to register.
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
