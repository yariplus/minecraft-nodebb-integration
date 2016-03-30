package com.yaricraft.nodebbintegration.socketio.listeners;

import io.socket.emitter.Emitter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 1/6/2016.
 */
public class ListenerWebChat implements Emitter.Listener
{
	@Override
	public void call(Object... args)
	{
		// Interpret message.
		if (args[0] != null)
		{
			try
			{
				final String name = ((JSONObject)args[0]).getString("name");
				final String message = ((JSONObject)args[0]).getString("message");
				Bukkit.broadcastMessage("[" + ChatColor.GOLD + "WEB" + ChatColor.RESET + "] <" + name + "> " + message);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
}
