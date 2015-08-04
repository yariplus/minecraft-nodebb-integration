package com.yaricraft.nodebbintegration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegister
{
    public static boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            String msg = "";
            for (int i = 0; i < CommandRegister.params.values().length; i++) {
                msg += " " + CommandRegister.params.values()[i].toString();
            }
            sender.sendMessage("Please use /register" + msg);
            return false;
        }else{
            new TaskRegister(sender, args).runTaskAsynchronously(NodeBBIntegration.getPlugin(JavaPlugin.class));
            return true;
        }
    }

    public enum params
    {
        EMAIL,
        PASSWORD
    }
}
