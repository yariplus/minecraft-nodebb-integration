package com.radiofreederp.nodebbintegration.bukkit.commands;

import com.radiofreederp.nodebbintegration.MinecraftServer;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.PluginConfig;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandNodeBBBukkit implements CommandExecutor {

    private final CommandNodeBB command;

    public CommandNodeBBBukkit(NodeBBIntegrationBukkit plugin) {
        this.command = new CommandNodeBB(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure source can receive messages back.
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return false;

        String action;
        String option = null;
        String value = null;

        switch (args.length) {
            default:
            case 0:
                action = "help";
                break;
            case 1:
                action = "get";
                option = args[0];
                break;
            case 2:
                action = "set";
                option = args[0];
                value = args[1].replace('_', ' ');
                break;
        }

        boolean success = this.command.doCommand(sender, action, option, value);

        return success;
    }
}
