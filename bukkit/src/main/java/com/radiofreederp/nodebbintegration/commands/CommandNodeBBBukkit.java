package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.PlayerManager;
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

        if (args.length == 0) {
            help(sender);
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                plugin.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                plugin.log("Reloaded Config.");
                SocketIOClient.connect();
                PlayerManager.reloadConfig();
            } else if (args[0].equals("debug")) {
                if (SocketIOClient.connected()) {
                    sender.sendMessage("MinecraftServer is connected to " + plugin.getConfig().getString("FORUMNAME") + " at " + plugin.getConfig().getString("FORUMURL"));
                } else {
                    sender.sendMessage("MinecraftServer is NOT connected to " + plugin.getConfig().getString("FORUMNAME") + " at " + plugin.getConfig().getString("FORUMURL"));
                }
                sender.sendMessage("The forum API key is set to " + plugin.getConfig().getString("APIKEY"));
                sender.sendMessage("Post bugs to https://goo.gl/qSy6BP");
                sender.sendMessage("Use " + ChatColor.YELLOW + "/nodebb debug toggle" + ChatColor.RESET + " to turn on verbose logging.");
            } else if (args[0].equals("key")) {
                sender.sendMessage("The forum API key is set to " + plugin.getConfig().getString("APIKEY"));
            } else if (args[0].equals("name")) {
                sender.sendMessage("The forum name is set to " + plugin.getConfig().getString("FORUMNAME"));
            } else if (args[0].equals("url")) {
                sender.sendMessage("The forum url is set to " + plugin.getConfig().getString("FORUMURL"));
            } else if (args[0].equals("live")) {
                for (String str : plugin.getConfig().getStringList("messages.nodebb.live.get")) {
                    sender.sendMessage(str.replace("%live%", plugin.getConfig().getString("socketio.address")));
                }
            } else {
                help(sender);
            }
        } else if (args.length >= 2) {
            if (args[0].equals("key")) {
                plugin.getConfig().set("APIKEY", args[1]);
                plugin.saveConfig();
                sender.sendMessage("Set new API key.");
                plugin.log("Set new API key.");
                SocketIOClient.connect();
            } else if (args[0].equals("name")) {
                String forumname = ChatColor.translateAlternateColorCodes('&', args[1]);
                for (int i = 2; i < args.length; i++) forumname += (" " + ChatColor.translateAlternateColorCodes('&', args[i]));
                plugin.getConfig().set("FORUMNAME", forumname);
                plugin.saveConfig();
                sender.sendMessage("Set forum name to " + forumname);
                plugin.log("Set forum name to " + forumname);
            } else if (args[0].equals("url")) {
                String url = ChatColor.translateAlternateColorCodes('&', args[1]);
                plugin.getConfig().set("FORUMURL", url);
                plugin.saveConfig();
                sender.sendMessage("Set forum url to " + url);
                plugin.log("Set forum url to " + url);
                SocketIOClient.connect();
            } else if (args[0].equals("live")) {
                plugin.getConfig().set("socketio.address", args[1]);
                plugin.saveConfig();
                for (String str : plugin.getConfig().getStringList("messages.nodebb.live.set")) {
                    sender.sendMessage(str.replace("%live%", args[1]));
                    plugin.log(str.replace("%live%", args[1]));
                }
                SocketIOClient.connect();
            } else if (args[0].equals("debug") && args[1].equals("toggle")) {
                NodeBBIntegrationBukkit.debug = !NodeBBIntegrationBukkit.debug;
                if (NodeBBIntegrationBukkit.debug) {
                    sender.sendMessage("Turned on verbose logging.");
                } else {
                    sender.sendMessage("Turned off verbose logging.");
                }
            } else {
                help(sender);
            }
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
