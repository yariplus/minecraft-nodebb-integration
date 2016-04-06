package com.radiofreederp.nodebbintegration.listeners;

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
        if (event.isVanishing()) {
            SocketIOClient.sendPlayerLeave(event.getPlayer());
        }else{
            SocketIOClient.sendPlayerJoin(event.getPlayer());
        }
    }
}
