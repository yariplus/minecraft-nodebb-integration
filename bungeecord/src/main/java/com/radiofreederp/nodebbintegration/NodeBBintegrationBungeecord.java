package com.radiofreederp.nodebbintegration;

/**
 * Created by Yari on 5/30/2016.
 */

import net.md_5.bungee.api.plugin.Plugin;

public class NodeBBIntegrationBungeeCord extends Plugin {
    @Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("Yay! It loads!");
    }
}
