package com.yaricraft.nodebbintegration;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandNodeBB implements CommandExecutor {
    private final NodeBBIntegration plugin;

    public CommandNodeBB(NodeBBIntegration plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            help(sender);
        } else if (args.length == 1) {
            NodeBBIntegration.log(String.valueOf(args[0].equals("reload")));
            if (args[0].equals("reload")) {
                plugin.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                NodeBBIntegration.log("Reloaded Config.");
                SocketIOClient.reconnect(sender);
            } else if (args[0].equals("debug")) {
                if (SocketIOClient.getSocket().connected()) {
                    sender.sendMessage("Server is connected to " + plugin.getConfig().getString("FORUMNAME") + " at " + plugin.getConfig().getString("FORUMURL"));
                } else {
                    sender.sendMessage("Server is NOT connected to " + plugin.getConfig().getString("FORUMNAME") + " at " + plugin.getConfig().getString("FORUMURL"));
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
                SocketIOClient.reconnect(sender);
            } else if (args[0].equals("name")) {
                String forumname = ChatColor.translateAlternateColorCodes('&', args[1]);
                for (int i = 2; i < args.length; i++) forumname += (" " + ChatColor.translateAlternateColorCodes('&', args[i]));
                forumname += ChatColor.RESET;
                plugin.getConfig().set("FORUMNAME", forumname);
                plugin.saveConfig();
                sender.sendMessage("Set forum name to " + forumname + ".");
                NodeBBIntegration.log("Set forum name to " + forumname + ".");
            } else if (args[0].equals("url")) {
                String url = ChatColor.translateAlternateColorCodes('&', args[1]);
                url += ChatColor.RESET;
                plugin.getConfig().set("FORUMURL", url);
                plugin.saveConfig();
                sender.sendMessage("Set forum url to " + url + ".");
                NodeBBIntegration.log("Set forum url to " + url + ".");
                SocketIOClient.reconnect(sender);
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
        sender.sendMessage("[" + ChatColor.GOLD + "NodeBB Integration" + ChatColor.WHITE + "]");
        sender.sendMessage(
                ChatColor.DARK_RED + "=" +
                ChatColor.RED + "=" +
                ChatColor.GOLD + "=" +
                ChatColor.YELLOW + "=" +
                ChatColor.GREEN + "=" +
                ChatColor.DARK_GREEN + "=" +
                ChatColor.DARK_AQUA + "=" +
                ChatColor.AQUA + "=" +
                ChatColor.BLUE + "=" +
                ChatColor.DARK_BLUE + "=" +
                ChatColor.LIGHT_PURPLE + "=" +
                ChatColor.DARK_PURPLE + "=" +
                ChatColor.DARK_RED + "=" +
                ChatColor.RED + "=" +
                ChatColor.GOLD + "=" +
                ChatColor.YELLOW + "=" +
                ChatColor.GREEN + "=" +
                ChatColor.DARK_GREEN + "=" +
                ChatColor.DARK_AQUA + "=" +
                ChatColor.AQUA + "=");
        sender.sendMessage("Commands:");
        sender.sendMessage(ChatColor.YELLOW + "/nodebb reload "      + ChatColor.RESET + "- Reloads config and reconnects to the forum.");
        sender.sendMessage(ChatColor.YELLOW + "/nodebb key [key] "   + ChatColor.RESET + "- Get or set the forum API key.");
        sender.sendMessage(ChatColor.YELLOW + "/nodebb name [name] " + ChatColor.RESET + "- Get or set the forum name.");
        sender.sendMessage(ChatColor.YELLOW + "/nodebb url [url] "   + ChatColor.RESET + "- Get or set the forum url.");
        sender.sendMessage(ChatColor.YELLOW + "/nodebb debug "       + ChatColor.RESET + "- Displays information useful for fixing errors.");
    }
}
