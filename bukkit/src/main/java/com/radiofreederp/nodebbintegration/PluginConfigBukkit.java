package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.ChatColor;

/**
 * Created by Yari on 4/8/2016.
 */
public class PluginConfigBukkit implements PluginConfig {
    private NodeBBIntegrationBukkit plugin = null;

    public PluginConfigBukkit(NodeBBIntegrationBukkit plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();

        if (!plugin.getConfig().getString("version").equals(plugin.getConfig().getDefaults().get("version"))) updateMessages();
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
        SocketIOClient.connect();
        PlayerManager.reloadConfig();
    }

    @Override
    public void save() {
        plugin.saveConfig();
        SocketIOClient.connect();
    }

    @Override
    public void updateMessages() {
        plugin.getConfig().set("messages", plugin.getConfig().getDefaults().get("messages"));
        plugin.getConfig().set("version", plugin.getConfig().getDefaults().get("version"));
        plugin.saveConfig();
    }

    private String getStripedKey(String key) {
        return ChatColor.stripColor(plugin.getConfig().getString(key));
    }

    @Override
    public String getForumURL() {
        return getStripedKey(ConfigOptions.FORUMURL.getKey());
    }

    @Override
    public String getForumName() {
        return getStripedKey(ConfigOptions.FORUMNAME.getKey());
    }

    @Override
    public String getSocketAddress() {
        return getStripedKey(ConfigOptions.SOCKETADDRESS.getKey());
    }

    @Override
    public String[] getSocketTransports() {
        return plugin.getConfig().getStringList(ConfigOptions.SOCKETTRANSPORTS.getKey()).toArray(new String[0]);
    }

    @Override
    public String getSocketNamespace() {
        return getStripedKey(ConfigOptions.SOCKETNAMESPACE.getKey()) + ".";
    }

    @Override
    public String getForumAPIKey() {
        return getStripedKey(ConfigOptions.FORUMAPIKEY.getKey());
    }

    @Override
    public void setForumURL(String url) {
        plugin.getConfig().set(ConfigOptions.FORUMURL.getKey(), url);
    }

    @Override
    public void setForumName(String name) {
        plugin.getConfig().set(ConfigOptions.FORUMNAME.getKey(), name);
    }

    @Override
    public void setForumAPIKey(String key) {
        plugin.getConfig().set(ConfigOptions.FORUMAPIKEY.getKey(), key);
    }

    @Override
    public void setSocketAddress(String address) {
        plugin.getConfig().set(ConfigOptions.SOCKETADDRESS.getKey(), address);
    }

    @Override
    public void addSocketTransport(String transport) {
        // TODO
    }

    @Override
    public void removeSocketTransport(String transport) {
        // TODO
    }

    @Override
    public String[] getMessage(String key) {
        return plugin.getConfig().getStringList(key).toArray(new String[0]);
    }

    @Override
    public void addMessageLine(String key, String line) {
        // TODO
    }

    @Override
    public void removeMessageLine(String key) {
        // TODO
    }
}
