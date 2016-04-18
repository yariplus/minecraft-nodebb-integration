package com.radiofreederp.nodebbintegration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Yari on 4/17/2016.
 */
public class BukkitServer extends MinecraftServer {
    private NodeBBIntegrationPlugin plugin;

    public BukkitServer(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    // Handle messaging.
    @Override
    public void sendMessage(Object receiver, String message) {
        ((CommandSender)receiver).sendMessage(translateColors(message));
    }
    @Override
    public void sendConsoleMessage(String message) {
        plugin.log(message);
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
