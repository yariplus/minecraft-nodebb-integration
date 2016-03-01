package com.yaricraft.nodebbintegration.hooks;

import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.listeners.ListenerVotifier;
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

    public static void hook(NodeBBIntegration plugin)
    {
        if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(new ListenerVotifier(), plugin);
            NodeBBIntegration.log("Hooked into Votifier.");
        }
    }
}
