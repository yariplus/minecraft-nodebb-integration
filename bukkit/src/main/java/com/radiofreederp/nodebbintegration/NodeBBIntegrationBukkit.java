package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.commands.CommandRegisterBukkit;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBBBukkit;
import com.radiofreederp.nodebbintegration.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.hooks.VotifierHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Level;

public class NodeBBIntegrationBukkit extends JavaPlugin implements NodeBBIntegrationPlugin {

    public TaskTick taskTick;

    public static NodeBBIntegrationBukkit instance;

    private PluginConfig pluginConfig;

    @Override
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    // Debug is initially true until the plugin is done loading.
    private boolean debug = true;
    @Override
    public boolean isDebug() {
        return debug;
    }
    @Override
    public void toggleDebug() {
        debug = !debug;
    }

    @Override
    public void log(String message) { log(message, Level.INFO); }
    @Override
    public void log(String message, Level level) {
        if (debug) {
            Bukkit.getLogger().log(level != null ? level : Level.INFO, "[NodeBB-Integration] " + message);
        }
    }

    // TODO: There's a better way to do this, but I can't figure it out.
    @Override
    public void runTaskAsynchronously(Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public void runTask(Runnable task) {
        new BukkitRunnable(){
            @Override
            public void run() {
                task.run();
            }
        }.runTask(this);
    }

    @Override
    public void eventWebChat(Object... args) {
        // Interpret message.
        if (args[0] != null)
        {
            try
            {
                final String name = ((JSONObject)args[0]).getString("name");
                final String message = ((JSONObject)args[0]).getString("message");
                Bukkit.broadcastMessage("[" + ChatColor.GOLD + "WEB" + ChatColor.RESET + "] <" + name + "> " + message);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void eventGetPlayerVotes(Object... args) {
        log("Got eventGetPlayerVotes");

        // Interpret message.
        if (args[0] == null) return;

        log("Compiling votes...");

        try
        {
            JSONObject json = (JSONObject)args[0];
            final String name = json.getString("name");

            JSONObject res = new JSONObject();
            res.put("name", name);
            res.put("key", NodeBBIntegrationBukkit.instance.getConfig().getString("APIKEY"));

            if (PlayerManager.getPlayerData().isConfigurationSection(name + ".voted"))
            {
                HashMap<String,Object> vm = (HashMap<String,Object>)PlayerManager.getPlayerData().getConfigurationSection(name + ".voted").getValues(false);
                res.put("votes", vm);
            }

            log("Sending votes...");
            SocketIOClient.emit("PlayerVotes", res, (Object... argsCB) -> {});
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {

        instance = this;

        // Start the socket client.
        SocketIOClient.create(this);

        // Monitor the TPS.
        taskTick = new TaskTick(this);

        // Loads config and updates if necessary.
        pluginConfig = new PluginConfigBukkit(this);

        // Load player data.
        PlayerManager.reloadConfig();

        // Initialize Plugin Hooks
        VaultHook.hook(this);
        OnTimeHook.hook(this);
        VotifierHook.hook(this);
        VanishNoPacketHook.hook(this);

        // Listen for Bukkit events.
        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(this), this);

        // Register commands.
        this.getCommand("nodebb").setExecutor(new CommandNodeBBBukkit(this));
        this.getCommand("register").setExecutor(new CommandRegisterBukkit(this));

        // Turn off debug messages after setup.
        debug = false;
    }

    @Override
    public void onDisable() {
        try {
            // Try to close the connection gracefully.
            SocketIOClient.close();
        } catch (Exception e) {
            // Any error doesn't matter.
        }
    }
}
