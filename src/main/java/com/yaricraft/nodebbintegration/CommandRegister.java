package com.yaricraft.nodebbintegration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandRegister extends Command
{
    NodeBBIntegration plugin;

    public CommandRegister(String name, NodeBBIntegration plugin)
    {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length != 2) {
            String msg = "";
            for (int i = 0; i < CommandRegister.params.values().length; i++) {
                msg += " " + CommandRegister.params.values()[i].toString();
            }
            commandSender.sendMessage("Please use /register" + msg);
        }else{
            new TaskRegister(commandSender, strings).runTaskAsynchronously(plugin);
        }
        return false;
    }

    public enum params
    {
        EMAIL,
        PASSWORD
    }
}
