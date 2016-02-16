package com.yaricraft.nodebbintegration.hooks;

import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.listeners.ListenerVanishNoPacket;
import org.bukkit.Bukkit;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

/**
 * Created by Yari on 2/14/2016.
 */
public final class VanishNoPacketHook {

    private VanishNoPacketHook(){}

    protected static boolean enabled = false;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void hook(NodeBBIntegration plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(new ListenerVanishNoPacket(), plugin);
            NodeBBIntegration.log("Hooked into VanishNoPacket.");
        }
    }

    public static boolean isVanished(String player)
    {
        try {
            return org.kitteh.vanish.staticaccess.VanishNoPacket.isVanished(player);
        } catch (VanishNotLoadedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
