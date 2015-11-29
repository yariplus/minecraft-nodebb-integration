package com.yaricraft.nodebbintegration;

import me.edge209.OnTime.OnTimeAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NodeBBIntegration extends JavaPlugin {

    public TaskTick taskTick;

    public static void log(String message) { log(message, Level.INFO); }
    public static void log(String message, Level level) { Bukkit.getLogger().log(level != null ? level : Level.INFO, "[NodeBB-Integration] " + message); }

    // OnTime
    public static boolean ontime = false;

    // Vault
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    @Override
    public void onEnable() {

        // Start the socket client.
        SocketIOClient.create(this).runTaskLaterAsynchronously(this, 60);

        // Monitor the TPS.
        taskTick = new TaskTick(this);

        // Create config.yml if new install.
        this.saveDefaultConfig();

        // Setup OnTime
        try {
            OnTimeAPI.data.values();
            ontime = true;
            log("OnTime found.");
        }catch (Exception e) {
            log("OnTime NOT found.");
        }

        // Setup Vault
        if (setupChat())        { log("Vault chat found.");        }else{ log("Vault chat NOT found."); }
        if (setupPermissions()) { log("Vault permissions found."); }else{ log("Vault permissions NOT found."); }
        if (setupEconomy())     { log("Vault economy found.");     }else{ log("Vault economy NOT found."); }

        // Listen for Bukkit events.
        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(this), this);

        this.getCommand("nodebb").setExecutor(new CommandNodeBB(this));
        this.getCommand("register").setExecutor(new CommandRegister(this));
    }

    @Override
    public void onDisable() {
        SocketIOClient.closeSocket();
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
