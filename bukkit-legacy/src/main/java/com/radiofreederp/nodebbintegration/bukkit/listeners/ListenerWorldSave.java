package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

/**
 * Created by Yari on 5/18/2016.
 */
public class ListenerWorldSave implements Listener {
    private NodeBBIntegrationBukkit plugin;

    public ListenerWorldSave(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
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
                    plugin.getPluginConfig().save();
                    plugin.log("Saved player data.");
                }
            }.runTask(plugin);
        }
    }
}
