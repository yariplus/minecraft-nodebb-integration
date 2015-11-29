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
            plugin.reloadConfig();
            sender.sendMessage("Reloaded Config.");
            NodeBBIntegration.log("Reloaded Config.");
        } else if (args.length == 1) {
            plugin.getConfig().set("APIKEY", args[0]);
            plugin.saveConfig();
            sender.sendMessage("Set new API key.");
            NodeBBIntegration.log("Set new API key.");
        }

        return true;
    }
}
