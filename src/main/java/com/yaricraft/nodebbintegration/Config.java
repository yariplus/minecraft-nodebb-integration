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

        if (version == null) {
            // Save current defaults.
            saveCurrent();
        }
    }

    private static void saveCurrent() {
        plugin.getConfig().set("version", "1.0.0");
        plugin.getConfig().set("PluginMessages.Register.AssertParameters", plugin.getConfig().getDefaults().get("PluginMessages.Register.AssertParameters"));
        plugin.getConfig().set("PluginMessages.Register.GiveProfile", plugin.getConfig().getDefaults().get("PluginMessages.Register.GiveProfile"));
        plugin.getConfig().set("PluginMessages.Register.FailPass", plugin.getConfig().getDefaults().get("PluginMessages.Register.FailPass"));
        plugin.saveConfig();
    }
}
