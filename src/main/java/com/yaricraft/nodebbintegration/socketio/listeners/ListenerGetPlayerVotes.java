package com.yaricraft.nodebbintegration.socketio.listeners;

import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.PlayerManager;
import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Yari on 1/6/2016.
 */
public class ListenerGetPlayerVotes implements Emitter.Listener
{
	@Override
	public void call(Object... args)
	{
		NodeBBIntegration.log("Got eventGetPlayerVotes");

		// Interpret message.
		if (args[0] == null) return;

		NodeBBIntegration.log("Compiling votes...");

		try
		{
			JSONObject json = (JSONObject)args[0];
			final String name = json.getString("name");

			JSONObject res = new JSONObject();
			res.put("name", name);
			res.put("key", NodeBBIntegration.instance.getConfig().getString("APIKEY"));

			if (PlayerManager.getPlayerData().isConfigurationSection(name + ".voted"))
			{
				HashMap<String,Object> vm = (HashMap<String,Object>)PlayerManager.getPlayerData().getConfigurationSection(name + ".voted").getValues(false);
				res.put("votes", vm);
			}

			NodeBBIntegration.log("Sending votes...");
			SocketIOClient.send(SocketIOClient.getNamespace() + "PlayerVotes", res);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
