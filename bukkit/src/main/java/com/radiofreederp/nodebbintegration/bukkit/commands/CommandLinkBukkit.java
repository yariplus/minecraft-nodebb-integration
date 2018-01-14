package com.radiofreederp.nodebbintegration.bukkit.commands;

import com.radiofreederp.nodebbintegration.commands.CommandLink;
import com.radiofreederp.nodebbintegration.commands.CommandRegister;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLinkBukkit extends CommandLink implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: MinecraftServerCommon.isPlayer();
        // Sender needs to be a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command needs to be run by a player.");
            return true;
        }

        return doCommand(sender, null, ((Player) sender).getUniqueId().toString(), sender.getName());
    }

    private String p(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
