package com.yaricraft.nodebbintegration.hooks;

import com.vexsoftware.votifier.Votifier;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.listeners.ListenerVotifier;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Yari on 12/29/2015.
 */
public final class VotifierHook {

    private VotifierHook(){}

    // Votifier available?
    public static boolean votifier = false;

    public static void hook(JavaPlugin plugin)
    {
        votifier = true;
        plugin.getServer().getPluginManager().registerEvents(new ListenerVotifier(), plugin);
        NodeBBIntegration.log("FOUND Votifier.");
    }

    public static void doThing()
    {
        NodeBBIntegration.log(Votifier.getInstance().getConfig().toString());
    }
}
