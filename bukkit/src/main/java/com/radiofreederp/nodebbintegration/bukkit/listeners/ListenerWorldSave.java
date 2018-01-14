package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.utils.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class ListenerWorldSave implements Listener {
    private NodeBBIntegrationPlugin plugin;
    private PluginConfig config;

    public ListenerWorldSave(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
        this.config = PluginConfig.instance;
    }

    // Allow the WorldSaveEvent to save the config once a minute.
    private long stamp = 0;
    @EventHandler
    public void onWorldSave(WorldSaveEvent event)
    {
        long now = new Date().getTime();
        if (now - stamp > 60000) {
            stamp = now;
            new BukkitRunnable() {
                @Override
                public void run() {
                    config.save();
                    Logger.log("Saved player data.");
                }
            }.runTask((NodeBBIntegrationBukkit)plugin);
        }
    }
}
