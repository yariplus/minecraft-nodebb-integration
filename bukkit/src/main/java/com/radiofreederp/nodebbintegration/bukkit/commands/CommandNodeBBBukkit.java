package com.radiofreederp.nodebbintegration.bukkit.commands;

import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class CommandNodeBBBukkit extends CommandNodeBB implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Make sure source can receive messages back.
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return false;

        String action;
        String option = null;
        String value = null;

        if (args.length == 0) {
            action = "help";
        } else if (args.length == 1) {
            action = "get";
            option = args[0];
        } else {
            action = "set";
            option = args[0];
            value = args[1].replace('_', ' ');
        }

        return doCommand(sender, action, option, value);
    }
}
