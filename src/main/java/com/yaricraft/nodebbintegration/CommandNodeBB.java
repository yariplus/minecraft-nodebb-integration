package com.yaricraft.nodebbintegration;

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
            System.out.println(args[0].equals("reload"));
            if (args[0].equals("reload")) {
                plugin.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                NodeBBIntegration.log("Reloaded Config.");
            } else if (args[0].equals("debug")) {
                sender.sendMessage("You API key is set to " + plugin.getConfig().getString("APIKEY"));
                sender.sendMessage("Post bugs to https://goo.gl/qSy6BP");
            } else {
                help(sender);
            }
        } else if (args.length == 2) {
            if (args[0].equals("key")) {
                plugin.getConfig().set("APIKEY", args[1]);
                plugin.saveConfig();
                sender.sendMessage("Set new API key.");
                NodeBBIntegration.log("Set new API key.");
            } else if (args[0].equals("name")) {
                plugin.getConfig().set("FORUMNAME", args[1]);
                plugin.saveConfig();
                sender.sendMessage("Set forum name to " + args[1] + ".");
                NodeBBIntegration.log("Set forum name to " + args[1] + ".");
            } else if (args[0].equals("url")) {
                plugin.getConfig().set("FORUMURL", args[1]);
                plugin.saveConfig();
                sender.sendMessage("Set forum url to " + args[1] + ".");
                NodeBBIntegration.log("Set forum url to " + args[1] + ".");
            } else {
                help(sender);
            }
        }

        return true;
    }

    private void help(CommandSender sender) {
        sender.sendMessage("[NodeBB Integration]");
        sender.sendMessage("====================");
        sender.sendMessage("Commands:");
        sender.sendMessage("/nodebb reload - Reloads config.yml from disk.");
        sender.sendMessage("/nodebb debug - Displays information useful for fixing errors.");
        sender.sendMessage("/nodebb key [key] - Set the forum API key.");
        sender.sendMessage("/nodebb name [name] - Set the forum name.");
        sender.sendMessage("/nodebb url [url] - Set the forum url.");
    }
}
