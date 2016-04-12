package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by Yari on 2/15/2016.
 */
public class ListenerVanishNoPacket implements Listener
{
    @EventHandler(priority=EventPriority.NORMAL)
    public void onVanishStatusChangeEvent(org.kitteh.vanish.event.VanishStatusChangeEvent event)
    {
        // TODO: Make more options for handling vanished players.
        if (event.isVanishing()) {
            SocketIOClient.emit(SocketIOClient.Events.onPlayerQuit, ListenerNodeBBIntegration.getPlayerQuitData(event.getPlayer()), args -> {});
        }else{
            SocketIOClient.emit(SocketIOClient.Events.onPlayerJoin, ListenerNodeBBIntegration.getPlayerJoinData(event.getPlayer()), args -> {});
        }
    }
}
