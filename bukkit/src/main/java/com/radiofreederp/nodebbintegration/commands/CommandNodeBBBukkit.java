package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandNodeBBBukkit implements CommandExecutor {
    private final NodeBBIntegrationBukkit plugin;
    public CommandNodeBBBukkit(NodeBBIntegrationBukkit plugin) { this.plugin = plugin; }

    List<String> NodeBBMessage = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args.length) {
            default:
            case 0:
                help(sender);
                break;
            case 1:
                switch (args[0]) {
                    case "reload":
                        plugin.getPluginConfig().reload();
                        sender.sendMessage("Reloaded PluginConfig.");
                        plugin.log("Reloaded PluginConfig.");
                        break;
                    case "debug":
                        if (SocketIOClient.connected()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "The server is currently &aCONNECTED to the forum."));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "The server is currently &4DISCONNECTED from the forum."));
                        }
                        sender.sendMessage("Forum Name is " + plugin.getPluginConfig().getForumName());
                        sender.sendMessage("Forum URL is " + plugin.getPluginConfig().getForumURL());
                        sender.sendMessage("Forum API Key is " + plugin.getPluginConfig().getForumAPIKey());
                        sender.sendMessage("Socket Live Address is " + plugin.getPluginConfig().getSocketAddress());
                        sender.sendMessage("Post bugs to https://goo.gl/qSy6BP");
                        sender.sendMessage("Use " + ChatColor.YELLOW + "/nodebb debug toggle" + ChatColor.RESET + " to toggle verbose logging.");
                        break;
                    case "name":
                        sender.sendMessage("Forum Name is " + plugin.getPluginConfig().getForumName());
                        break;
                    case "url":
                        sender.sendMessage("Forum URL is " + plugin.getPluginConfig().getForumURL());
                        break;
                    case "key":
                        sender.sendMessage("Forum API Key is " + plugin.getPluginConfig().getForumAPIKey());
                        break;
                    case "live":
                        for (String str : plugin.getPluginConfig().getMessage(PluginConfig.ConfigOptions.MSG_SOCKETADDRESS_GET.getKey())) {
                            sender.sendMessage(str.replace("%live%", plugin.getPluginConfig().getSocketAddress()));
                        }
                        break;
                    default:
                    case "help":
                        help(sender);
                        break;
                }
                break;
            case 2:
                String value = args[1];
                switch (args[0]) {
                    case "debug":
                        plugin.toggleDebug();
                        if (plugin.isDebug()) {
                            sender.sendMessage("Turned on verbose logging.");
                        } else {
                            sender.sendMessage("Turned off verbose logging.");
                        }
                        break;
                    case "name":
                        value = ChatColor.translateAlternateColorCodes('&', value);
                        // TODO: Better implementation of a name with spaces.
                        for (int i = 2; i < args.length; i++) value += (" " + ChatColor.translateAlternateColorCodes('&', args[i]));
                        plugin.getPluginConfig().setForumName(value);
                        sender.sendMessage("Set forum name to " + value);
                        plugin.log("Set forum name to " + value);
                        plugin.getPluginConfig().save();
                        break;
                    case "url":
                        value = ChatColor.translateAlternateColorCodes('&', value);
                        plugin.getPluginConfig().setForumURL(value);
                        sender.sendMessage("Set forum url to " + value);
                        plugin.log("Set forum url to " + value);
                        plugin.getPluginConfig().save();
                        break;
                    case "key":
                        plugin.getPluginConfig().setForumAPIKey(value);
                        sender.sendMessage("Set new API key.");
                        plugin.log("Set new API key.");
                        plugin.getPluginConfig().save();
                        break;
                    case "live":
                        plugin.getPluginConfig().setSocketAddress(value);
                        for (String str : plugin.getConfig().getStringList("messages.nodebb.live.set")) {
                            sender.sendMessage(str.replace("%live%", value));
                            plugin.log(str.replace("%live%", value));
                        }
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

    private void help(CommandSender sender) {
        if (NodeBBMessage == null) {
            NodeBBMessage = plugin.getConfig().getStringList("PluginMessages.NodeBB.Help");
        }
        for (String str : NodeBBMessage) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
        }
    }
}
