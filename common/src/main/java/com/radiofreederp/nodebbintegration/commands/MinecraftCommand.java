package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.MinecraftServer;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;

/**
 * Created by Yari on 4/18/2016.
 */
public abstract class MinecraftCommand implements IMinecraftCommand {
    protected final NodeBBIntegrationPlugin plugin;
    protected final MinecraftServer server;

    protected MinecraftCommand(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getMinecraftServer();
    }
}
