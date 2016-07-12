package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.bukkit.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Yari on 5/18/2016.
 */
public class ListenerPlayerJoin implements Listener {
    private NodeBBIntegrationBukkit plugin;

    public ListenerPlayerJoin(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Ignore vanished players.
        if (VanishNoPacketHook.isEnabled()) {
            if (VanishNoPacketHook.isVanished(event.getPlayer().getName())) return;
        }

        MinecraftServerEvents.onPlayerJoin(plugin, event.getPlayer(), getPlayerJoinData(event.getPlayer()));
    }

    public static JSONObject getPlayerJoinData(Player player) {

        JSONObject obj = new JSONObject();

        try {
            obj.put("name", player.getName());
            obj.put("id", player.getUniqueId());
            obj.put("key", NodeBBIntegrationBukkit.instance.getPluginConfig().getForumAPIKey());

            if (OnTimeHook.isEnabled()) {
                if (OnTimeHook.isEnabled()) {
                    OnTimeHook.onTimeCheckTime(player, obj);
                }
            }

            if (VaultHook.chat != null && VaultHook.permission != null) {
                String[] groupNames = VaultHook.permission.getPlayerGroups(null, player);
                final JSONArray groups = new JSONArray();
                final World world = Bukkit.getWorlds().get(0);

                obj.put("primaryGroup", VaultHook.chat.getPrimaryGroup(player));
                obj.put("prefix", VaultHook.chat.getPlayerPrefix(player));
                obj.put("suffix", VaultHook.chat.getPlayerSuffix(player));

                for (String groupName : groupNames) {
                    try {
                        JSONObject group = new JSONObject();

                        group.put("name", groupName);
                        group.put("prefix", VaultHook.chat.getGroupPrefix(world, groupName));
                        group.put("suffix", VaultHook.chat.getGroupSuffix(world, groupName));

                        groups.put(group);
                    } catch (JSONException ignored) {
                    }
                }

                obj.put("groups", groups);
            }

        } catch (JSONException e) {
            NodeBBIntegrationBukkit.instance.log("Error constructing JSON Object for " + SocketIOClient.Events.onPlayerJoin);
            e.printStackTrace();
        }

        return obj;
    }
}
