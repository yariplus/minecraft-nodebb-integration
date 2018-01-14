package com.radiofreederp.nodebbintegration.bukkit.hooks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.bukkit.listeners.ListenerVanishNoPacket;
import com.radiofreederp.nodebbintegration.utils.Logger;
import org.bukkit.Bukkit;

/**
 * Created by Yari on 2/14/2016.
 */
public final class VanishNoPacketHook {

    private VanishNoPacketHook(){}

    protected static boolean enabled = false;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void hook(NodeBBIntegrationBukkit plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")) {
            enabled = true;
            plugin.getServer().getPluginManager().registerEvents(new ListenerVanishNoPacket(plugin), plugin);
            Logger.log("Hooked into VanishNoPacket.");
        }
    }

    public static boolean isVanished(String player)
    {
        try {
            return org.kitteh.vanish.staticaccess.VanishNoPacket.isVanished(player);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
