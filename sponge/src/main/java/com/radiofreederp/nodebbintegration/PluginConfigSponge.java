package com.radiofreederp.nodebbintegration;

import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.function.Function;

/**
 * Created by Yari on 4/8/2016.
 */
public class PluginConfigSponge implements PluginConfig {
    private NodeBBIntegrationSponge plugin = null;

    public PluginConfigSponge(NodeBBIntegrationSponge plugin) {
        this.plugin = plugin;

        if (!plugin.getSpongeConfig().getNode("version").getString().equals(plugin.getDefaultConfig().getNode("version").getString())) updateMessages();
    }

    @Override
    public void reload() {

    }

    @Override
    public void save() {
        try {
            plugin.getConfigManager().save(plugin.getSpongeConfig());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMessages() {
        plugin.getSpongeConfig().getNode("messages").setValue(plugin.getDefaultConfig().getNode("messages"));
        plugin.getSpongeConfig().getNode("version").setValue(plugin.getDefaultConfig().getNode("version").getString());
        save();
    }

    private String getStripedKey(String key) {
        return Text.of(getString(key.split("\\."))).toPlain();
    }

    private String getString(Object... objects) {
        String value = plugin.getSpongeConfig().getNode(objects).getString();

        // If null, set to default.
        if (value == null) {
            value = plugin.getDefaultConfig().getNode(objects).getString();
            plugin.getSpongeConfig().getNode(objects).setValue(value);
            save();
        }
        return value;
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
    public String getForumAPIKey() {
        return getStripedKey(ConfigOptions.FORUMAPIKEY.getKey());
    }

    @Override
    public String getSocketAddress() {
        return getStripedKey(ConfigOptions.SOCKETADDRESS.getKey());
    }

    @Override
    public String[] getSocketTransports() {
        return plugin.getSpongeConfig().getNode(ConfigOptions.SOCKETTRANSPORTS.getKey().split("\\.")).getList(obj -> (String)obj).toArray(new String[0]);
    }

    @Override
    public String getSocketNamespace() {
        return getStripedKey(ConfigOptions.SOCKETNAMESPACE.getKey()) + ".";
    }

    @Override
    public void setForumURL(String url) {
        plugin.getSpongeConfig().getNode(ConfigOptions.FORUMURL.getKey().split("\\.")).setValue(url);
    }

    @Override
    public void setForumName(String name) {
        plugin.getSpongeConfig().getNode(ConfigOptions.FORUMNAME.getKey().split("\\.")).setValue(name);
    }

    @Override
    public void setForumAPIKey(String key) {
        plugin.getSpongeConfig().getNode(ConfigOptions.FORUMAPIKEY.getKey().split("\\.")).setValue(key);
    }

    @Override
    public void setSocketAddress(String address) {
        plugin.getSpongeConfig().getNode(ConfigOptions.SOCKETADDRESS.getKey().split("\\.")).setValue(address);
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
    public String[] getMessage(String key)
    {
        // TODO
        return new String[0];
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
