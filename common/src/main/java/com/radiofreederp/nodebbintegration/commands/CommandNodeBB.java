package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;

import java.util.HashMap;

/**
 * Created by Yari on 4/17/2016.
 */
public class CommandNodeBB extends MinecraftCommand {

    public CommandNodeBB(NodeBBIntegrationPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean doCommand(Object sender, String action, String option, String value) {

        switch (action) {
            default:
            case "help":
                help(sender);
                break;
            case "get":
                switch (option) {
                    case "reload":
                        plugin.getPluginConfig().reload();
                        server.sendMessage(sender, "Reloaded PluginConfig.");
                        server.sendConsoleMessage("Reloaded PluginConfig.");
                        break;
                    case "debug":
                        if (SocketIOClient.connected()) {
                            server.sendMessage(sender, "The server is currently &aCONNECTED to the forum.");
                        } else {
                            server.sendMessage(sender, "The server is currently &4DISCONNECTED from the forum.");
                        }
                        server.sendMessage(sender, "Forum Name is " + plugin.getPluginConfig().getForumName());
                        server.sendMessage(sender, "Forum URL is " + plugin.getPluginConfig().getForumURL());
                        server.sendMessage(sender, "Forum API Key is " + plugin.getPluginConfig().getForumAPIKey());
                        server.sendMessage(sender, "Socket Live Address is " + plugin.getPluginConfig().getSocketAddress());
                        server.sendMessage(sender, "Post bugs to https://goo.gl/qSy6BP");
                        server.sendMessage(sender, "Use &3/nodebb debug toggle&r to toggle verbose logging.");
                        break;
                    case "name":
                        server.sendMessage(sender, "Forum Name is " + plugin.getPluginConfig().getForumName());
                        break;
                    case "url":
                        server.sendMessage(sender, "Forum URL is " + plugin.getPluginConfig().getForumURL());
                        break;
                    case "key":
                        server.sendMessage(sender, "Forum API Key is " + plugin.getPluginConfig().getForumAPIKey());
                        break;
                    case "live":
                        HashMap<String, String> vars = new HashMap<>();
                        vars.put("%live%", plugin.getPluginConfig().getSocketAddress());
                        server.sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_SOCKETADDRESS_GET), vars);
                        break;
                    default:
                    case "help":
                        help(sender);
                        break;
                }
                break;
            case "set":
                switch (option) {
                    case "debug":
                        plugin.toggleDebug();
                        if (plugin.isDebug()) {
                            server.sendMessage(sender, "Turned on verbose logging.");
                        } else {
                            server.sendMessage(sender, "Turned off verbose logging.");
                        }
                        break;
                    case "name":
                        plugin.getPluginConfig().setForumName(value);
                        server.sendMessage(sender, "Set forum name to " + value);
                        plugin.log("Set forum name to " + value);
                        plugin.getPluginConfig().save();
                        break;
                    case "url":
                        plugin.getPluginConfig().setForumURL(value);
                        server.sendMessage(sender, "Set forum url to " + value);
                        plugin.log("Set forum url to " + value);
                        plugin.getPluginConfig().save();
                        break;
                    case "key":
                        plugin.getPluginConfig().setForumAPIKey(value);
                        server.sendMessage(sender, "Set new API key.");
                        plugin.log("Set new API key.");
                        plugin.getPluginConfig().save();
                        break;
                    case "live":
                        plugin.getPluginConfig().setSocketAddress(value);
                        HashMap<String, String> vars = new HashMap<>();
                        vars.put("%live%", plugin.getPluginConfig().getSocketAddress());
                        server.sendMessage(sender, plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_SOCKETADDRESS_SET), vars);
                        server.sendConsoleMessage(plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_SOCKETADDRESS_SET), vars);
                        plugin.getPluginConfig().save();
                        break;
                    default:
                    case "help":
                        help(sender);
                        break;
                }
                break;
        }

        return true;
    }

    private void help(Object sender) {
        for (String str : plugin.getPluginConfig().getArray(PluginConfig.ConfigOption.MSG_HELP)) {
            plugin.getMinecraftServer().sendMessage(sender, str);
        }
    }
}
