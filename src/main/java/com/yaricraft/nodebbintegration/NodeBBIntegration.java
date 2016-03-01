package com.yaricraft.nodebbintegration;

import com.yaricraft.nodebbintegration.commands.CommandNodeBB;
import com.yaricraft.nodebbintegration.commands.CommandRegister;
import com.yaricraft.nodebbintegration.hooks.OnTimeHook;
import com.yaricraft.nodebbintegration.hooks.VanishNoPacketHook;
import com.yaricraft.nodebbintegration.hooks.VaultHook;
import com.yaricraft.nodebbintegration.hooks.VotifierHook;
import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
import com.yaricraft.nodebbintegration.tasks.TaskTick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NodeBBIntegration extends JavaPlugin {

    public TaskTick taskTick;

    public static NodeBBIntegration instance;

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

        // Create config.yml if new install.
        this.saveDefaultConfig();

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
