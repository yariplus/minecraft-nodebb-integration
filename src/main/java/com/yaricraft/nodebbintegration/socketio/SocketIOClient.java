package com.yaricraft.nodebbintegration.socketio;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.hooks.OnTimeHook;
import com.yaricraft.nodebbintegration.hooks.VanishNoPacketHook;
import com.yaricraft.nodebbintegration.hooks.VaultHook;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerGetPlayerVotes;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerWebChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

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

	public static void sendPlayerJoin(Player player) {
		if (getSocket() == null) return;
		final String socketEvent = getNamespace() + "eventPlayerJoin";

		if (VanishNoPacketHook.isEnabled()) {
			if (VanishNoPacketHook.isVanished(player.getName())) return;
		}

		JSONObject obj = new JSONObject();
		try {
			obj.put("name", player.getName());
			obj.put("id", player.getUniqueId());
			obj.put("key", plugin.getConfig().getString("APIKEY"));

			if (OnTimeHook.isEnabled()) {
				if (OnTimeHook.isEnabled()) {
					OnTimeHook.onTimeCheckTime(player, obj);
				}
			}

			if (VaultHook.chat != null && VaultHook.permission != null) {
				String[] groups = VaultHook.permission.getPlayerGroups(null, player);

				World world = Bukkit.getWorlds().get(0);
				HashMap<String,Object> groupsData = new HashMap<String,Object>();

				for (int i = 0; i < groups.length; i++)
				{
					groupsData.put(groups[i], VaultHook.chat.getGroupPrefix(world, groups[i]));
				}
				obj.put("groups", groupsData);
				obj.put("prefix", VaultHook.chat.getPlayerPrefix(player));
			}

		} catch (JSONException e) {
			NodeBBIntegration.log("Error constructing JSON Object for " + socketEvent);
			e.printStackTrace();
			return;
		}

		NodeBBIntegration.log("Sending " + socketEvent);
		SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegration.log(socketEvent + " callback received.");
			}
		});
	}

	public static void sendPlayerLeave(Player player) {
		if (getSocket() == null) return;
		final String socketEvent = getNamespace() + "eventPlayerQuit";

		JSONObject obj = new JSONObject();
		try {
			obj.put("name", player.getName());
			obj.put("id", player.getUniqueId());
			obj.put("key", plugin.getConfig().getString("APIKEY"));
		} catch (JSONException e) {
			NodeBBIntegration.log("Error constructing JSON Object for " + socketEvent);
			e.printStackTrace();
			return;
		}

		NodeBBIntegration.log("Sending " + socketEvent);
		SocketIOClient.getSocket().emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegration.log(socketEvent + " callback received.");
			}
		});
	}
}
