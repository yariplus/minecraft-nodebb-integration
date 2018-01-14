package com.radiofreederp.nodebbintegration.bukkit.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.commands.CommandRegister;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandRegisterBukkit implements CommandExecutor
{
    private final CommandRegister command;
    private final NodeBBIntegrationPlugin plugin;

    public CommandRegisterBukkit(NodeBBIntegrationBukkit plugin) {
        this.command = new CommandRegister(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // TODO: MinecraftServerCommon.isPlayer();
        // Sender needs to be a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command needs to be run by a player.");
            return true;
        }

        return this.command.doCommand(sender, args.length == 1 ? args[0] : null, ((Player) sender).getUniqueId().toString(), sender.getName());
    }

    private String p(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
