package com.radiofreederp.nodebbintegration;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.radiofreederp.nodebbintegration.bukkit.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.utils.Logger;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class BukkitServer extends MinecraftServerCommon {
    private NodeBBIntegrationPlugin plugin;

    public BukkitServer(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    // Handle messaging.
    @Override
    public void sendMessage(Object receiver, String message) {
        ((CommandSender)receiver).sendMessage(translateColors("&f[&7n&9BB&7i&f] &r" + message));
    }
    @Override
    public void sendConsoleMessage(String message) {
        Logger.log(removeColors(message));
    }
    @Override
    public void sendMessageToOps(String message) {
        Bukkit.getOnlinePlayers().stream().filter(player->player.hasPermission("nodebb.admin")).forEach(op->sendMessage(op, "&d" + message));
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

            playerList.add(getPlayerJSON(player));
        }

        return playerList;
    }

    @Override
    public JSONObject getPlayerJSON (Object _player) {
        JSONObject playerObj = new JSONObject();
        Player player = (Player)_player;

        try {
            playerObj.put("name", player.getName());
            playerObj.put("displayName", player.getDisplayName());
            playerObj.put("id", player.getUniqueId());

            if (VaultHook.chat != null && VaultHook.permission != null) {
                playerObj.put("primaryGroup", VaultHook.chat.getPrimaryGroup(player));
                playerObj.put("prefix", VaultHook.chat.getPlayerPrefix(player));
                playerObj.put("suffix", VaultHook.chat.getPlayerSuffix(player));
                playerObj.put("groups", VaultHook.permission.getPlayerGroups(null, player));
            }

            if (OnTimeHook.isEnabled()) {
                if (OnTimeHook.isEnabled()) {
                    OnTimeHook.onTimeCheckTime(player, playerObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return playerObj;
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
    public String getPlayerPrefix(Object _player) {
        Player player = (Player)_player;

        if (VaultHook.chat != null) {
            return VaultHook.chat.getPlayerPrefix(player);
        } else {
            return null;
        }
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
        if (!VaultHook.permission.hasGroupSupport()) return data;

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

    @Override
    public JSONArray getScoreboards() {
        Set<OfflinePlayer> players = Bukkit.getScoreboardManager().getMainScoreboard().getPlayers();
        Set<Objective> objectives = Bukkit.getScoreboardManager().getMainScoreboard().getObjectives();

        JSONArray jObjectives = new JSONArray();

        objectives.forEach(objective -> {
            JSONObject jObjective = new JSONObject();
            JSONArray jEntries = new JSONArray();

            players.forEach(player -> {
                if (objective.getScore(player) != null) {
                    JSONObject jEntry = new JSONObject();

                    try {
                        jEntry.put("id", player.getUniqueId().toString());
                        jEntry.put("name", player.getName());
                        jEntry.put("score", objective.getScore(player).getScore());
                        jEntries.put(jEntry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                jObjective.put("name", objective.getName());
                jObjective.put("displayname", objective.getDisplayName());
                jObjective.put("criteria", objective.getCriteria());
                jObjective.put("displayslot", objective.getDisplaySlot());
                jObjective.put("entries", jEntries);
                jObjectives.put(jObjective);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return jObjectives;
    }
}
