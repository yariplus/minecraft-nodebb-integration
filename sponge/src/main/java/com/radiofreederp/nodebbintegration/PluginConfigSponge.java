package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Yari on 4/8/2016.
 */
public class PluginConfigSponge extends PluginConfig {

    public PluginConfigSponge(NodeBBIntegrationSponge plugin) {
        this.plugin = plugin;

        if (!plugin.getSpongeConfig().getNode("version").getString().equals(plugin.getDefaultConfig().getNode("version").getString())) updateMessages();
    }

    private NodeBBIntegrationSponge getPlugin() {
        return (NodeBBIntegrationSponge)this.plugin;
    }

    @Override
    public void reload() {
        // TODO
        SocketIOClient.connect();
    }

    @Override
    public void save() {
        try {
            getPlugin().getConfigManager().save(getPlugin().getSpongeConfig());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMessages() {
        getPlugin().getSpongeConfig().getNode("messages").setValue(getPlugin().getDefaultConfig().getNode("messages"));
        getPlugin().getSpongeConfig().getNode("version").setValue(getPlugin().getDefaultConfig().getNode("version").getString());
        save();
    }

    @Override
    public String getString(PluginConfig.ConfigOption option) {
        return getPlugin().getSpongeConfig().getNode(option.getKey().split("\\.")).getString();
    }

    @Override
    public List<String> getArray(PluginConfig.ConfigOption option) {
        return getPlugin().getSpongeConfig().getNode(option.getKey().split("\\.")).getList(obj -> (String)obj);
    }

    @Override
    public void setString(PluginConfig.ConfigOption option, String value) {
        getPlugin().getSpongeConfig().getNode(option.getKey().split("\\.")).setValue(value);
    }

    @Override
    public void setArray(PluginConfig.ConfigOption option, List<String> value) {
        getPlugin().getSpongeConfig().getNode(option.getKey().split("\\.")).setValue(value);
    }


    @Override
    public Object getPlayerData() {
        return null;
    }

    @Override
    public JSONObject getPlayerVotes(JSONObject req) {
        return null;
    }
}
