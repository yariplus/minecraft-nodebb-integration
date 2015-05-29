package com.yaricraft.nodebbintegration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class NodeBBIntegration extends JavaPlugin {
    private enum defaults {
        URL("https://community.example.com/register/mc"),
        FORUMNAME("https://community.example.com/"),
        KEY("SECRETPASSWORD"),
        APIHOSTNAME("localhost"),
        APIPORT("25665");

        public String value;

        defaults(String value) {
            this.value = value;
        }
    }

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        SocketServer.create(this).runTaskLater(this, 20);

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
