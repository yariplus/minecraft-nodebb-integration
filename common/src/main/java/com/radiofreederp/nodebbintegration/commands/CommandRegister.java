package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import io.socket.client.Ack;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 4/18/2016.
 */
public class CommandRegister extends MinecraftCommand {

    public CommandRegister(NodeBBIntegrationPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean doCommand(final Object sender, String pkey, String uuid, String name) {

        // Assert parameters.
        if (pkey == null) {
            plugin.getMinecraftServer().sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_REG_ASSERTPARAMS), plugin.getPluginConfig().getConfigMap());
            return true;
        }

        // Trim key.
        if (pkey.length() > 4) pkey = pkey.substring(4);

        // Alert
        plugin.getMinecraftServer().sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_REG_ALERT), plugin.getPluginConfig().getConfigMap());

        // If we're not connected, don't do anything.
        if (SocketIOClient.disconnected()) {
            plugin.getMinecraftServer().sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_REG_DISCONNECTED));
            return true;
        }

        // Construct request object.
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", plugin.getPluginConfig().getForumAPIKey());
            obj.put("id", uuid);
            obj.put("name", name);
            obj.put("pkey", pkey);
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }

        plugin.log("Sending commandRegister");

        SocketIOClient.emit("commandRegister", obj, new Ack() {
            @Override
            public void call(Object... res) {

                plugin.log("Received commandRegister callback");

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

                // Send response message.
                server.sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.valueOf("MSG_REG_" + result)), plugin.getPluginConfig().getConfigMap());
            }
        });

        return false;
    }
}
