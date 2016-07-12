package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import org.bukkit.event.Listener;

/**
 * Created by Yari on 5/28/2015.
 */
abstract public class ListenerNodeBBIntegration implements Listener {
    protected NodeBBIntegrationBukkit plugin;

    public ListenerNodeBBIntegration(NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
    }
}
