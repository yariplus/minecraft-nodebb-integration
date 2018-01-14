package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ListenerVanishNoPacket implements Listener
{
    private NodeBBIntegrationBukkit plugin;

    public ListenerVanishNoPacket (NodeBBIntegrationPlugin plugin) {
        this.plugin = (NodeBBIntegrationBukkit)plugin;
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onVanishStatusChangeEvent(org.kitteh.vanish.event.VanishStatusChangeEvent event)
    {
        // TODO: Make more options for handling vanished players.
        if (event.isVanishing()) {
            SocketIOClient.emit(SocketIOClient.Events.onPlayerQuit, plugin.getMinecraftServer().getPlayerJSON(event.getPlayer()), args -> {});
        }else{
            SocketIOClient.emit(SocketIOClient.Events.onPlayerJoin, plugin.getMinecraftServer().getPlayerJSON(event.getPlayer()), args -> {});
        }
    }
}
