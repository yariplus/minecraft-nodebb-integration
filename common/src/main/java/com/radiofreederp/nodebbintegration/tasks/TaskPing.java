package com.radiofreederp.nodebbintegration.tasks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.socketio.ESocketEvent;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskPing implements Runnable {

	private static TaskPing instance;

	public TaskPing (NodeBBIntegrationPlugin plugin) {
		instance = this;

		plugin.runTaskTimerAsynchronously(this, 20 * 10, 20);
	}

	public static TaskPing getTask () {
		return instance;
	}

	@Override
	public void run () {
		if (SocketIOClient.disconnected()) return;

		final String socketEvent = ESocketEvent.SERVER_PING;

		JSONObject timestamp = new JSONObject();

		try {
			timestamp.put("timestamp", System.currentTimeMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SocketIOClient.emit(socketEvent, timestamp, null);
	}
}
