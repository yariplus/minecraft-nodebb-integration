package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.commands.CommandRegister;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import com.radiofreederp.nodebbintegration.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.hooks.VotifierHook;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NodeBBIntegrationBukkit extends JavaPlugin implements NodeBBIntegrationPlugin {

    public TaskTick taskTick;

    public static NodeBBIntegrationBukkit instance;

    // Logging
    public static boolean debug = true;
    public static void log(String message) { log(message, Level.INFO); }
    public static void log(String message, Level level) {
        if (debug) {
            Bukkit.getLogger().log(level != null ? level : Level.INFO, "[NodeBB-Integration] " + message);
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
        Config.load();

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
        this.getCommand("nodebb").setExecutor(new CommandNodeBB(this));
        this.getCommand("register").setExecutor(new CommandRegister(this));

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
