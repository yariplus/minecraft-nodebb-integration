package com.yaricraft.nodebbintegration;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Yari on 5/28/2015.
 */
public class NodeBBIntegrationListener implements Listener {
    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        String data = event.getPlayer().getUniqueId() + ":" + event.getPlayer().getName();
        System.out.println("Sending data PlayerJoin, " + data);
        SocketServer.getInstance().emitPlayerJoin(data);
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        String data = event.getPlayer().getUniqueId() + ":" + event.getPlayer().getName();
        System.out.println("Sending data PlayerQuit, " + data);
        SocketServer.getInstance().emitPlayerQuit(data);
    }
}
