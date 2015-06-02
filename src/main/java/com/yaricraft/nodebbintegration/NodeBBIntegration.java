package com.yaricraft.nodebbintegration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class NodeBBIntegration extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        SocketServer.create(this).runTaskLaterAsynchronously(this, 20);

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
