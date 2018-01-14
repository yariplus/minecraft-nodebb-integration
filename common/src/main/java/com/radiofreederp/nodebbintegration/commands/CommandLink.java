package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.Messages;
import com.radiofreederp.nodebbintegration.utils.Logger;
import io.socket.client.Ack;
import org.json.JSONException;
import org.json.JSONObject;

public class CommandLink extends MinecraftCommand {

    public CommandLink() {
        super();
    }

    @Override
    public boolean doCommand(final Object sender, String pkey, String uuid, String name) {

        plugin.getMinecraftServer().sendMessage(sender, "Linking...");

        // If we're not connected, don't do anything.
        if (SocketIOClient.disconnected()) {
            plugin.getMinecraftServer().sendMessage(sender, Messages.DISCONNECTED);
            return true;
        }

        // Construct request object.
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", config.getForumAPIKey());
            obj.put("id", uuid);
            obj.put("name", name);
            obj.put("prefix", server.getPlayerPrefix(sender));
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }

        Logger.log("Sending link command");

        SocketIOClient.emit("keygen", obj, new Ack() {
            @Override
            public void call(Object... res) {

                Logger.log("Received link callback");

                String key;

                // Interpret response object.
                try {
                    if (res[0] != null) {
                        plugin.getMinecraftServer().sendConsoleMessage(((JSONObject) res[0]).getString("message"));
                        plugin.getMinecraftServer().sendMessage(sender, ((JSONObject) res[0]).getString("message"));
                        return;
                    } else {
                        key = ((JSONObject) res[1]).getString("key");
                    }
                } catch (Exception e) {
                    // TODO
                    e.printStackTrace();
                    plugin.getMinecraftServer().sendMessage(sender, Messages.DISCONNECTED);
                    return;
                }

                // TODO: Send response message.
                server.sendMessage(sender, Messages.LINK);
                server.sendMessage(sender, config.getForumURL() + "/m/link/" + key);
            }
        });

        return false;
    }
}
