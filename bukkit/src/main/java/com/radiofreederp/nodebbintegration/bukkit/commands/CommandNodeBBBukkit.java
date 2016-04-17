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

import java.util.List;
import java.util.UUID;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandNodeBBBukkit implements CommandExecutor {

    private final NodeBBIntegrationBukkit plugin;
    private final MinecraftServer server;
    private final CommandNodeBB command;

    public CommandNodeBBBukkit(NodeBBIntegrationBukkit plugin) {
        this.plugin = plugin;
        this.server = plugin.getMinecraftServer();
        this.command = new CommandNodeBB(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String action;
        String value = null;

        switch (args.length) {
            default:
            case 0:
                action = "help";
                break;
            case 1:
                action = "get";
                break;
            case 2:
                action = "set";
                break;
        }

        boolean success = this.command.doCommand(action, value, null, null);

        return success;
    }
}
