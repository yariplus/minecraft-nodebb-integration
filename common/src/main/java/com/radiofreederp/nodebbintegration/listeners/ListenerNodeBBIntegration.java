package com.radiofreederp.nodebbintegration.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;

abstract public class ListenerNodeBBIntegration {
    protected NodeBBIntegrationPlugin plugin;

    public ListenerNodeBBIntegration(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }
}
