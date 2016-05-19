package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.bukkit.commands.CommandRegisterBukkit;
import com.radiofreederp.nodebbintegration.bukkit.commands.CommandNodeBBBukkit;
import com.radiofreederp.nodebbintegration.bukkit.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.bukkit.hooks.VotifierHook;
import com.radiofreederp.nodebbintegration.bukkit.listeners.*;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;

public class NodeBBIntegrationBukkit extends JavaPlugin implements NodeBBIntegrationPlugin {

    public static NodeBBIntegrationBukkit instance;

    private MinecraftServer minecraftServer = new BukkitServer(this);

    @Override
    public MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

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
    public void runTaskTimerAsynchronously(Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskTimerAsynchronously(this, 20 * 60, 20 * 60);
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
    public void initTaskTick() {
        new TaskTick(this);
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
    public void eventGetPlayerVotes(Object... req) {
        log("Got eventGetPlayerVotes");

        // Interpret message.
        // TODO: Should be an error object.
        if (req[0] == null) return;

        log("Compiling votes...");
        JSONObject res = getPluginConfig().getPlayerVotes((JSONObject)req[0]);

        log("Sending votes...");
        SocketIOClient.emit("PlayerVotes", res, cb -> {});

    }

    @Override
    public void onEnable() {

        instance = this;

        // Start the socket client.
        SocketIOClient.create(this);

        // Monitor the TPS.
        initTaskTick();

        // Loads config and updates if necessary.
        pluginConfig = new PluginConfigBukkit(this);

        // Initialize Plugin Hooks
        VaultHook.hook(this);
        OnTimeHook.hook(this);
        VotifierHook.hook(this);
        VanishNoPacketHook.hook(this);

        // Register listeners.
        getServer().getPluginManager().registerEvents(new ListenerPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new ListenerServerListPing(this), this);
        getServer().getPluginManager().registerEvents(new ListenerWorldSave(this), this);

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
