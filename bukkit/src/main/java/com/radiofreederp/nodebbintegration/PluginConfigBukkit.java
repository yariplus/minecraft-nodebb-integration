package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;

import java.util.List;

/**
 * Created by Yari on 4/8/2016.
 */
public class PluginConfigBukkit extends PluginConfig {

    public PluginConfigBukkit(NodeBBIntegrationBukkit plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();

        if (!plugin.getConfig().getString("version").equals(plugin.getConfig().getDefaults().get("version"))) updateMessages();
    }

    private NodeBBIntegrationBukkit getPlugin() {
        return (NodeBBIntegrationBukkit)this.plugin;
    }

    @Override
    public void reload() {
        getPlugin().reloadConfig();
        SocketIOClient.connect();
        PlayerManager.reloadConfig();
    }

    @Override
    public void save() {
        getPlugin().saveConfig();
        SocketIOClient.connect();
    }

    @Override
    public void updateMessages() {
        getPlugin().getConfig().set("messages", getPlugin().getConfig().getDefaults().get("messages"));
        getPlugin().getConfig().set("version", getPlugin().getConfig().getDefaults().get("version"));
        getPlugin().saveConfig();
    }

    @Override
    public String getString(ConfigOption option) {
        return getPlugin().getConfig().getString(option.getKey(), "{unset}");
    }

    @Override
    public List<String> getArray(ConfigOption option) {
        return getPlugin().getConfig().getStringList(option.getKey());
    }

    @Override
    public void setString(ConfigOption option, String value) {
        getPlugin().getConfig().set(option.getKey(), value);
    }

    @Override
    public void setArray(ConfigOption option, List<String> value) {
        getPlugin().getConfig().set(option.getKey(), value);
    }
}
