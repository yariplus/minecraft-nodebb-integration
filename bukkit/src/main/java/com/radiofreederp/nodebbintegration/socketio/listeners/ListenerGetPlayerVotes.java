package com.radiofreederp.nodebbintegration.socketio.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.PlayerManager;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import io.socket.client.Ack;
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
		NodeBBIntegrationBukkit.log("Got eventGetPlayerVotes");

		// Interpret message.
		if (args[0] == null) return;

		NodeBBIntegrationBukkit.log("Compiling votes...");

		try
		{
			JSONObject json = (JSONObject)args[0];
			final String name = json.getString("name");

			JSONObject res = new JSONObject();
			res.put("name", name);
			res.put("key", NodeBBIntegrationBukkit.instance.getConfig().getString("APIKEY"));

			if (PlayerManager.getPlayerData().isConfigurationSection(name + ".voted"))
			{
				HashMap<String,Object> vm = (HashMap<String,Object>)PlayerManager.getPlayerData().getConfigurationSection(name + ".voted").getValues(false);
				res.put("votes", vm);
			}

			NodeBBIntegrationBukkit.log("Sending votes...");
			SocketIOClient.emit("PlayerVotes", res, new Ack() {
				@Override
				public void call(Object... objects) {

				}
			});
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
