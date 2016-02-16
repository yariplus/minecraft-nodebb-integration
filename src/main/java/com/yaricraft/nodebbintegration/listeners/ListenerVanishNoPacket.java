package com.yaricraft.nodebbintegration.listeners;

import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

/**
 * Created by Yari on 2/15/2016.
 */
public class ListenerVanishNoPacket implements Listener
{
    @EventHandler(priority=EventPriority.NORMAL)
    public void onVanishStatusChangeEvent(VanishStatusChangeEvent event)
    {
        if (event.isVanishing()) {
            SocketIOClient.sendPlayerLeave(event.getPlayer());
        }else{
            SocketIOClient.sendPlayerJoin(event.getPlayer());
        }
    }
}
