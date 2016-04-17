package com.radiofreederp.nodebbintegration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yari on 4/17/2016.
 */
public class BukkitServer implements MinecraftServer {
    private NodeBBIntegrationPlugin plugin;

    public BukkitServer(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    // Send single message.
    @Override
    public void sendMessage(Object receiver, String message) {
        ((CommandSender)receiver).sendMessage(translateColors(message));
    }
    @Override
    public void sendMessage(Object receiver, String message, HashMap<String, String> vars) {
        vars.forEach((k,v)->message.replace(k,v));
        ((CommandSender)receiver).sendMessage(translateColors(message));
    }
    @Override
    public void sendConsoleMessage(String message) {
        plugin.log(message);
    }
    @Override
    public void sendConsoleMessage(String message, HashMap<String, String> vars) {
        vars.forEach((k,v)->message.replace(k,v));
        plugin.log(message);
    }

    // Send message list.
    @Override
    public void sendMessage(Object receiver, List<String> messages) {
        messages.forEach(message->sendMessage(receiver, message));
    }
    @Override
    public void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendMessage(receiver, message, vars));
    }
    @Override
    public void sendConsoleMessage(List<String> messages) {
        messages.forEach(message->sendConsoleMessage(message));
    }
    @Override
    public void sendConsoleMessage(List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendConsoleMessage(message, vars));
    }

    // Handle color.
    @Override
    public String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    @Override
    public String removeColors(String string) {
        return ChatColor.stripColor(translateColors(string));
    }
}
