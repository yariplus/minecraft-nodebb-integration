package com.yaricraft.nodebbintegration;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NodeBBIntegration extends JavaPlugin {

    public TaskTick taskTick;

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
