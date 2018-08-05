package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.ESocketEvent;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.Messages;
import com.radiofreederp.nodebbintegration.utils.Logger;
import io.socket.client.Ack;

import java.util.Arrays;
import java.util.HashMap;

public class CommandNodeBB extends MinecraftCommand {

    public CommandNodeBB() {
        super();
    }

	private HashMap<String, String> vars = new HashMap<>();

    @Override
    public boolean doCommand(Object sender, String action, String option, String value) {
		vars.clear();

        switch (action) {
            default:
            case "help":
                help(sender);
                break;
            case "get":
                switch (option) {
                    case "reload":
                        config.reload();
                        server.sendMessage(sender, "Reloaded PluginConfig.");
                        server.sendConsoleMessage("Reloaded PluginConfig.");

                        // Reconnect socket client with new settings.
                        SocketIOClient.connect();
                        break;
                    case "syncgroups":
                        // Sync Players
                        plugin.runTask(new Runnable() {
                            @Override
                            public void run() {
                                SocketIOClient.emit(ESocketEvent.RANKS_WITH_MEMBERS, plugin.getMinecraftServer().getGroupsWithMembers(), new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        Logger.log("Received " + ESocketEvent.RANKS_WITH_MEMBERS + " callback.");
                                    }
                                });
                            }
                        });
                        break;
                    case "debug":
                        PluginConfig.setDebug(!PluginConfig.getDebug());
                        if (PluginConfig.getDebug()) {
                            server.sendMessage(sender, "Turned on verbose logging.");
                            server.sendMessage(sender, "Post bugs to https://goo.gl/qSy6BP");
                        } else {
                            server.sendMessage(sender, "Turned off verbose logging.");
                        }
                        break;
                    case "settings":
                        if (SocketIOClient.connected()) {
                            server.sendMessage(sender, "The server is currently &aCONNECTED to the forum.");
                        } else {
                            server.sendMessage(sender, "The server is currently &4DISCONNECTED from the forum.");
                        }
                        server.sendMessage(sender, "Forum Name is " + config.getForumName());
                        server.sendMessage(sender, "Forum URL is " + config.getForumURL());
                        server.sendMessage(sender, "Forum API Key is " + config.getForumAPIKey());
                        server.sendMessage(sender, "socket.io address is " + config.getSocketAddress());
                        server.sendMessage(sender, "socket.io namespace is " + config.getSocketNamespace());
                        server.sendMessage(sender, "socket.io transports is " + config.getSocketTransportsAsString());
                        break;
                    case "name":
                        server.sendMessage(sender, "Forum Name is " + config.getForumName());
                        break;
                    case "url":
                        server.sendMessage(sender, "Forum URL is " + config.getForumURL());
                        break;
                    case "key":
                        server.sendMessage(sender, "Forum API Key is " + config.getForumAPIKey());
                        break;
                    case "live":
					case "siourl":
					case "socketurl":
					case "socket":
						server.sendMessage(sender, "socket.io address is " + config.getSocketAddress());
                        break;
					case "namespace":
						server.sendMessage(sender, "socket.io namespace is " + config.getSocketNamespace());
						break;
					case "transports":
						server.sendMessage(sender, "socket.io transports is " + config.getSocketTransportsAsString());
						break;
                    case "reset":
                        config.reset();
                        server.sendMessage(sender, "Set settings to defaults.");
                        server.sendConsoleMessage("Set settings to defaults.");
                        break;
                    default:
                    case "help":
                        help(sender);
                        break;
                }
                break;
            case "set":
                switch (option) {
                    case "name":
                        config.setForumName(value);
                        server.sendMessage(sender, "Set forum name to " + value);
                        Logger.log("Set forum name to " + value);
                        config.save();
                        break;
                    case "url":
                        if (value.indexOf("https://") != 0) value = "https://" + value;
                        if (value.charAt(value.length() - 1) != '/') value = value + "/";
                        config.setForumURL(value);
                        config.setSocketAddress(value);
                        server.sendMessage(sender, "Set forum url to " + value);
                        Logger.log("Set forum url to " + value);
                        config.save();
                        break;
                    case "key":
                        config.setForumAPIKey(value);
                        server.sendMessage(sender, "Set new API key: " + value);
                        Logger.log("Set new API key: " + value);
                        config.save();
                        SocketIOClient.connect();
                        break;
                    case "live":
                    case "siourl":
                    case "socketurl":
                    case "socket":
                        if (value.indexOf("https://") != 0) value = "https://" + value;
                        if (value.charAt(value.length() - 1) != '/') value = value + "/";
                        config.setSocketAddress(value);
                        HashMap<String, String> vars = new HashMap<>();
                        vars.put("%live%", config.getSocketAddress());
                        server.sendMessage(sender, Messages.SOCKETIO_DOMAIN_SET, vars);
                        server.sendConsoleMessage(Messages.SOCKETIO_DOMAIN_SET, vars);
                        config.save();
                        break;
                    case "namespace":
                        config.setSocketNamespace(value);
                        server.sendMessage(sender, "Set new Socket Namespace: " + value);
                        Logger.log("Set new Socket Namespace: " + value);
                        config.save();
                        break;
                    default:
                    case "help":
                        // TODO: Errors for invalid keys.
                        help(sender);
                        break;
                }
                break;
        }

        return true;
    }

    private void help(Object sender) {
        plugin.getMinecraftServer().sendMessage(sender, Arrays.asList(Messages.HELP));
    }
}
