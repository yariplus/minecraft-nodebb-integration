package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Yari on 4/8/2016.
 */
public class PluginConfigBukkit extends PluginConfig {

    private YamlConfiguration playerData;
    private File playerFile;

    public PluginConfigBukkit(NodeBBIntegrationBukkit plugin) {
        this.plugin = plugin;

        // Main config.
        plugin.saveDefaultConfig();

        // Player data.
        playerFile = new File(this.getPlugin().getDataFolder(), "players.yml");
        playerData = YamlConfiguration.loadConfiguration(playerFile);

        // Re-write messages if version updated.
        // TODO: Preserve custom messages.
        if (!plugin.getConfig().getString("version").equals(plugin.getConfig().getDefaults().get("version"))) updateMessages();
    }

    private NodeBBIntegrationBukkit getPlugin() {
        return (NodeBBIntegrationBukkit)this.plugin;
    }

    @Override
    public void reload() {
        // Main config.
        getPlugin().reloadConfig();

        // Player data
        playerFile = new File(((NodeBBIntegrationBukkit)plugin).getDataFolder(), "players.yml");
        playerData = YamlConfiguration.loadConfiguration(playerFile);

        // Reconnect socket.
        SocketIOClient.connect();
    }

    @Override
    public void save() {
        // Main config.
        getPlugin().saveConfig();

        // Player data.
        plugin.log("Saving player data.");
        try {
            playerData.save(playerFile);
        } catch (IOException ex) {
            plugin.log("Could not save player data to " + playerFile.getName(), Level.SEVERE);
            ex.printStackTrace();
        }

        // Reconnect socket.
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

    @Override
    public Object getPlayerData()
    {
        if (playerData == null) reload();
        return playerData;
    }

    @Override
    public JSONObject getPlayerVotes(JSONObject req) {
        if (playerData == null) reload();

        JSONObject res = new JSONObject();

        try {
            final String name = req.getString("name");

            res.put("key", plugin.getPluginConfig().getForumAPIKey());
            res.put("name", name);

            if (playerData.isConfigurationSection(name + ".voted"))
            {
                HashMap<String,Object> vm = (HashMap<String,Object>)playerData.getConfigurationSection(name + ".voted").getValues(false);
                res.put("votes", vm);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return res;
    }
}
