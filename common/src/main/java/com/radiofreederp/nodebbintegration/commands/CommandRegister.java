package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.Messages;
import com.radiofreederp.nodebbintegration.utils.ConfigMap;
import com.radiofreederp.nodebbintegration.utils.Logger;
import io.socket.client.Ack;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CommandRegister extends MinecraftCommand {

    public CommandRegister() {
        super();
    }

    @Override
    public boolean doCommand(final Object sender, String pkey, String uuid, String name) {

        // If no key, remind them how to use the command.
        if (pkey == null) {
            plugin.getMinecraftServer().sendMessage(sender, Arrays.asList(Messages.HELP_REGISTER), new ConfigMap<String, String>().add("%forumurl%", PluginConfig.instance.getForumURL()).add("%forumname%", PluginConfig.instance.getForumName()));
            return true;
        }

        // Trim the prefix "key-" from the key.
        if (pkey.length() > 4) pkey = pkey.substring(4);

        // TODO: Insert helpful message here.
        plugin.getMinecraftServer().sendMessage(sender, Messages.REGISTER);

        // If we're not connected, don't do anything.
        if (SocketIOClient.disconnected()) {
            plugin.getMinecraftServer().sendMessage(sender, Messages.DISCONNECTED);
            return true;
        }

        // Construct request object.
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", PluginConfig.instance.getForumAPIKey());
            obj.put("id", uuid);
            obj.put("name", name);
            obj.put("pkey", pkey);
            obj.put("prefix", server.getPlayerPrefix(sender));
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }

        Logger.log("Sending commandRegister");

        SocketIOClient.emit("commandRegister", obj, new Ack() {
            @Override
            public void call(Object... res) {

                Logger.log("Received commandRegister callback");

                // Default is error.
                String result = "ERROR";

                // Interpret response object.
                try {
                    if (res[0] != null) {
                        result = ((JSONObject) res[0]).getString("message");
                    } else {
                        result = ((JSONObject) res[1]).getString("result");
                    }
                } catch (Exception e) {
                    result = "BADRES";
                }

                // TODO: Send response message.
                server.sendMessage(sender, "Result is " + result);
            }
        });

        return false;
    }
}
