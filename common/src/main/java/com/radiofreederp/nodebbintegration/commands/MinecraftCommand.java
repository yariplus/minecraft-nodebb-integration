package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.MinecraftServerCommon;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;

public abstract class MinecraftCommand implements IMinecraftCommand {
    protected final NodeBBIntegrationPlugin plugin;
    protected final MinecraftServerCommon server;
    protected final PluginConfig config;

    protected MinecraftCommand() {
        this.plugin = NBBPlugin.instance;
        this.server = NBBPlugin.instance.getMinecraftServer();
        this.config = PluginConfig.instance;
    }
}
