package com.yaricraft.nodebbintegration;

import com.yaricraft.nodebbintegration.commands.CommandNodeBB;
import com.yaricraft.nodebbintegration.commands.CommandRegister;
import com.yaricraft.nodebbintegration.hooks.OnTimeHook;
import com.yaricraft.nodebbintegration.hooks.VaultHook;
import com.yaricraft.nodebbintegration.hooks.VotifierHook;
import com.yaricraft.nodebbintegration.tasks.TaskTick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NodeBBIntegration extends JavaPlugin {

    public TaskTick taskTick;

    public static NodeBBIntegration instance;

    // Debug
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
        SocketIOClient.create(this).runTaskLaterAsynchronously(this, 60);

        // Monitor the TPS.
        taskTick = new TaskTick(this);

        // Create config.yml if new install.
        this.saveDefaultConfig();

        // Setup Vault
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            VaultHook.hook(this);
        }
        else log("Vault NOT found.");

        // Setup OnTime
        if (Bukkit.getPluginManager().isPluginEnabled("OnTime")) {
            OnTimeHook.hook();
        }
        else log("OnTime NOT found.");

        // Setup Votifier
        if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            VotifierHook.hook(this);
        }
        else log("Votifier NOT found.");

        // Listen for Bukkit events.
        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(this), this);

        this.getCommand("nodebb").setExecutor(new CommandNodeBB(this));
        this.getCommand("register").setExecutor(new CommandRegister(this));

        // Turn off debug messages after setup.
        debug = false;
    }

    @Override
    public void onDisable() {
        SocketIOClient.closeSocket();
    }
}
