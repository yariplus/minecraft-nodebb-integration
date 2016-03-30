package com.yaricraft.nodebbintegration;

/**
 * Created by Yari on 3/6/2016.
 */
public class Config {
    private static NodeBBIntegration plugin = null;

    public static void load() {
        plugin = NodeBBIntegration.instance;

        plugin.saveDefaultConfig();

        String version = plugin.getConfig().getString("version");

        // TEMP
        saveCurrent();
    }

    private static void saveCurrent() {
        plugin.getConfig().set("PluginMessages.Register.Alert", plugin.getConfig().getDefaults().get("PluginMessages.Register.Alert"));
        plugin.getConfig().set("PluginMessages.Register.AssertParameters", plugin.getConfig().getDefaults().get("PluginMessages.Register.AssertParameters"));
        plugin.getConfig().set("PluginMessages.Register.FailKey", plugin.getConfig().getDefaults().get("PluginMessages.Register.FailKey"));
        plugin.saveConfig();
    }
}
