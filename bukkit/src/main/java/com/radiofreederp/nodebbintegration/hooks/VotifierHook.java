package com.radiofreederp.nodebbintegration.hooks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.listeners.ListenerVotifier;
import org.bukkit.Bukkit;

/**
 * Created by Yari on 12/29/2015.
 */
public final class VotifierHook {

    private VotifierHook(){}

    private static boolean enabled = false;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void hook(NodeBBIntegrationBukkit plugin)
    {
        if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(new ListenerVotifier(), plugin);
            NodeBBIntegrationBukkit.instance.log("Hooked into Votifier.");
        }
    }
}
