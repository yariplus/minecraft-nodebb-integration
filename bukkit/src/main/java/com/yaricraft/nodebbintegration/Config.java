package com.yaricraft.nodebbintegration;

/**
 * Created by Yari on 3/6/2016.
 */
public class Config {
    private static NodeBBIntegration plugin = null;

    public static void load() {
        plugin = NodeBBIntegration.instance;

        plugin.saveDefaultConfig();

        if (!plugin.getConfig().getString("version").equals(plugin.getConfig().getDefaults().get("version"))) saveCurrent();
    }

    private static void saveCurrent() {
        plugin.getConfig().set("messages", plugin.getConfig().getDefaults().get("messages"));
        plugin.getConfig().set("PluginMessages.Register.Alert", plugin.getConfig().getDefaults().get("PluginMessages.Register.Alert"));
        plugin.getConfig().set("PluginMessages.Register.AssertParameters", plugin.getConfig().getDefaults().get("PluginMessages.Register.AssertParameters"));
        plugin.getConfig().set("PluginMessages.Register.FailKey", plugin.getConfig().getDefaults().get("PluginMessages.Register.FailKey"));
        plugin.saveConfig();
    }
}
