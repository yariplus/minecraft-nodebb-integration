package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.bukkit.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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

        MinecraftServerEvents.onPlayerJoin(event.getPlayer(), plugin.getMinecraftServer().getPlayerJSON(event.getPlayer()));
    }
}
