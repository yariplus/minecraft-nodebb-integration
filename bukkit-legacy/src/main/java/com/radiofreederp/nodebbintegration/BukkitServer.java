package com.radiofreederp.nodebbintegration;

import com.google.common.io.Files;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Yari on 4/17/2016.
 */
public class BukkitServer extends MinecraftServerCommon {
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
        plugin.log(removeColors(message));
    }
    @Override
    public void sendMessageToOps(final String message) {
        if (plugin.isDebug()) {
            for ( Player player : Bukkit.getOnlinePlayers() ) {
                if (player.hasPermission("nodebb.admin")) sendMessage(player, message);
            }
        }
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

    // Get TPS
    // TODO: Replace hackery when a better method is found.
    private static Object minecraftServer;
    private static Field recentTps;
    public String getTPS() {
        try {
            if (minecraftServer == null) {
                Server server = Bukkit.getServer();
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                minecraftServer = consoleField.get(server);
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return String.valueOf(((double[]) recentTps.get(minecraftServer))[0]);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return "0.000";
    }

    @Override
    public ArrayList<JSONObject> getPlayerList() {

        final ArrayList<JSONObject> playerList = new ArrayList<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            if (VanishNoPacketHook.isEnabled()) {
                if (VanishNoPacketHook.isVanished(player.getName())) continue;
            }

            JSONObject playerObj = new JSONObject();

            try {
                playerObj.put("name", player.getName());
                playerObj.put("id", player.getUniqueId());

                playerList.add(playerObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return playerList;
    }

    @Override
    public ArrayList<JSONObject> getPluginList() {

        final ArrayList<JSONObject> pluginList = new ArrayList<>();

        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {

            JSONObject pluginObj = new JSONObject();

            try {
                pluginObj.put("name", plugin.getName());
                pluginObj.put("version", plugin.getDescription().getVersion());

                pluginList.add(pluginObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return pluginList;
    }

    @Override
    public String getVersion() {
        return Bukkit.getVersion();
    }

    @Override
    public String getServerName() {
        return Bukkit.getServerName();
    }

    @Override
    public String getServerIcon() {
        // TODO
        return null;
    }

    @Override
    public String getWorldType() {
        return Bukkit.getWorldType();
    }

    @Override
    public String getWorldName() {
        return Bukkit.getWorlds().get(0).getName();
    }

    @Override
    public String getMotd() {
        return Bukkit.getMotd();
    }

    @Override
    public int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().length;
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }
}
