package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerPlayerQuit implements Listener {

    private NodeBBIntegrationBukkit plugin;

    public ListenerPlayerQuit(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MinecraftServerEvents.onPlayerQuit(event.getPlayer(), plugin.getMinecraftServer().getPlayerJSON(event.getPlayer()));
    }
}
