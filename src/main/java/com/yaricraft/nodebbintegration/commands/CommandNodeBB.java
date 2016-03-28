package com.yaricraft.nodebbintegration.commands;

import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.PlayerManager;
import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
import com.yaricraft.nodebbintegration.hooks.VotifierHook;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerGetPlayerVotes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandNodeBB implements CommandExecutor {
    private final NodeBBIntegration plugin;
    List<String> NodeBBMessage = null;
    public CommandNodeBB(NodeBBIntegration plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            help(sender);
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                plugin.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                NodeBBIntegration.log("Reloaded Config.");
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
            } else {
                help(sender);
            }
        } else if (args.length >= 2) {
            if (args[0].equals("key")) {
                plugin.getConfig().set("APIKEY", args[1]);
                plugin.saveConfig();
                sender.sendMessage("Set new API key.");
                NodeBBIntegration.log("Set new API key.");
                SocketIOClient.connect();
            } else if (args[0].equals("name")) {
                String forumname = ChatColor.translateAlternateColorCodes('&', args[1]);
                for (int i = 2; i < args.length; i++) forumname += (" " + ChatColor.translateAlternateColorCodes('&', args[i]));
                plugin.getConfig().set("FORUMNAME", forumname);
                plugin.saveConfig();
                sender.sendMessage("Set forum name to " + forumname);
                NodeBBIntegration.log("Set forum name to " + forumname);
            } else if (args[0].equals("url")) {
                String url = ChatColor.translateAlternateColorCodes('&', args[1]);
                plugin.getConfig().set("FORUMURL", url);
                plugin.saveConfig();
                sender.sendMessage("Set forum url to " + url);
                NodeBBIntegration.log("Set forum url to " + url);
                SocketIOClient.connect();
            } else if (args[0].equals("debug") && args[1].equals("toggle")) {
                NodeBBIntegration.debug = !NodeBBIntegration.debug;
                if (NodeBBIntegration.debug) {
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
