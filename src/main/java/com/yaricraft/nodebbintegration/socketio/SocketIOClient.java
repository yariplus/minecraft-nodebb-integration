package com.yaricraft.nodebbintegration.socketio;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerGetPlayerVotes;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerWebChat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIOClient extends BukkitRunnable {

	private static SocketIOClient instance;

	private static JavaPlugin plugin;
	private static Socket socket;
	public static String id;

	public static String getNamespace() {
		String ns = plugin.getConfig().getString("SOCKETNAMESPACE");
		String pl = plugin.getConfig().getString("PLUGINID");

		return ns + "." + pl + ".";
	}

	private SocketIOClient(JavaPlugin _plugin) {
		plugin = _plugin;
	}

	public static SocketIOClient create(JavaPlugin plugin) {
		if (instance == null) instance = new SocketIOClient(plugin);
		return instance;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void closeSocket() {
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (socket == null) {
			try {
				socket = IO.socket(ChatColor.stripColor(plugin.getConfig().getString("FORUMURL")));
				NodeBBIntegration.log("Success! The socket client was created.");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}else{
			NodeBBIntegration.log("Oops, something went wrong with the socket client. I tried to start a duplicate instance.");
		}

		setOptions();
		socket.connect();
	}

	public static void reconnect()
	{
		if(socket.connected()) closeSocket();
		try {
			socket = IO.socket(ChatColor.stripColor(plugin.getConfig().getString("FORUMURL")));
			NodeBBIntegration.log("Success! The socket client was created.");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		setOptions();
		socket.connect();
	}

	public static void reconnect(final CommandSender sender) {
		new BukkitRunnable() {
			@Override
			public void run() {
				sender.sendMessage("Re-establishing socket connection...");
				reconnect();

				new BukkitRunnable() {
					@Override
					public void run() {
						if(socket.connected()) {
							sender.sendMessage("Successfully connected to the forum.");
						}else{
							sender.sendMessage("Error connecting to the forum.");
						}
					}
				}.runTaskLater(plugin, 40);
			}
		}.runTaskLaterAsynchronously(plugin, 40);
	}

	private static void setOptions()
	{
		if (socket != null)
		{
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener()
			{
				@Override
				public void call(Object... args) {
					sync();
				}
			}).on(Socket.EVENT_RECONNECT, new Emitter.Listener()
			{
				@Override
				public void call(Object... args)
				{
					NodeBBIntegration.log("Re-connected to the forum.");
				}
			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener()
			{
				@Override
				public void call(Object... args)
				{
					NodeBBIntegration.log("Lost connection to the forum. Will try to re-connect later.");
				}
			});

			socket.on("eventWebChat", new ListenerWebChat());
			socket.on("eventGetPlayerVotes", new ListenerGetPlayerVotes());

		}else{
			NodeBBIntegration.log("Uh Oh. I failed to create a socket client. Are you sure the forum url is correct?");
		}
	}

	// Socket events are sent asynchronously.
	public static void send(final String event, final JSONObject object)
	{
		NodeBBIntegration.log("Sending " + event + " to the forum.");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(socket.connected())
				{
					NodeBBIntegration.log("Finished sending " + event + ".");
					socket.emit(event, object);
				}
				else
				{
					NodeBBIntegration.log("Socket disconnected, couldn't send " + event);
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	private static void sync() {
		NodeBBIntegration.log("Connected to the forum!");
		id = socket.id();
		((NodeBBIntegration)plugin).taskTick.run();
	}
}
