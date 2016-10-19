package com.radiofreederp.nodebbintegration;

import com.google.common.io.Files;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Consumer;

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
    public void sendMessageToOps(String message) {
        if (plugin.isDebug()) Bukkit.getOnlinePlayers().stream().filter(player->player.hasPermission("nodebb.admin")).forEach(op->sendMessage(op, message));
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

        String icon = "";

        File file = new File("server-icon.png");
        if (file.isFile()) {
            try {
                icon = "data:image/png;base64," + Base64.getEncoder().encodeToString(Files.toByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return icon;
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
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    private JSONArray getPlayerGroups(OfflinePlayer player) {
        JSONArray groups = new JSONArray();

        if (VaultHook.permission == null) return groups;

        Arrays.stream(VaultHook.permission.getPlayerGroups(Bukkit.getWorlds().get(0).getName(), player)).forEach(g -> {
            try {
                JSONObject group = new JSONObject();

                group.put("name", g);

                groups.put(group);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return groups;
    }

    @Override
    public JSONObject getGroups() {
        JSONObject groups = new JSONObject();

        if (VaultHook.permission == null) return groups;

        Arrays.stream(VaultHook.permission.getGroups()).forEach(g -> {
            JSONObject group = new JSONObject();

            try {
                group.put("name", g);
                group.put("members", new JSONArray());

                groups.put(g, group);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return groups;
    }

    @Override
    public JSONObject getGroupsWithMembers() {
        JSONObject data = new JSONObject();

        if (VaultHook.permission == null) return data;

        JSONObject groupsObj = getGroups();

        Arrays.stream(Bukkit.getOfflinePlayers()).forEach(p -> Arrays.stream(VaultHook.permission.getPlayerGroups(Bukkit.getWorlds().get(0).getName(), p)).forEach(g -> {
            try {
                JSONObject player = new JSONObject();

                player.put("id", p.getUniqueId());
                player.put("name", p.getName());
                player.put("lastplayed", p.getLastPlayed());

                groupsObj.getJSONObject(g).getJSONArray("members").put(player);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        try {
            data.put("ranks", groupsObj.toJSONArray(groupsObj.names()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public JSONObject getOfflinePlayers() {
        JSONObject data = new JSONObject();

        JSONArray players = new JSONArray();

        Arrays.stream(Bukkit.getOfflinePlayers()).forEach(offlinePlayer -> {
            JSONObject player = new JSONObject();

            try {
                player.put("id", offlinePlayer.getUniqueId());
                player.put("name", offlinePlayer.getName());
                player.put("lastPlayed", offlinePlayer.getLastPlayed());
                player.put("groups", getPlayerGroups(offlinePlayer));

                players.put(player);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        try {
            data.put("players", players);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
