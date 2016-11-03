package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.bukkit.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONException;
import org.json.JSONObject;

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

        MinecraftServerEvents.onPlayerJoin(plugin, event.getPlayer(), plugin.getMinecraftServer().getPlayerJSON(event.getPlayer()));
    }
}
