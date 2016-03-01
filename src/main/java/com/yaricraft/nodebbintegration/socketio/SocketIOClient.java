package com.yaricraft.nodebbintegration.socketio;

import io.socket.emitter.Emitter;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.hooks.OnTimeHook;
import com.yaricraft.nodebbintegration.hooks.VanishNoPacketHook;
import com.yaricraft.nodebbintegration.hooks.VaultHook;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerGetPlayerVotes;
import com.yaricraft.nodebbintegration.socketio.listeners.ListenerWebChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

public final class SocketIOClient {

	private static SocketIOClient instance;
	private static NodeBBIntegration plugin;
	private static Socket socket;

	public static String id;

	// TODO: This doesn't belong here.
	public static String getNamespace() {
		String ns = plugin.getConfig().getString("SOCKETNAMESPACE");
		String pl = plugin.getConfig().getString("PLUGINID");

		return ns + "." + pl + ".";
	}

	// Initial connection when created.
	private SocketIOClient(NodeBBIntegration _plugin) {
		plugin = _plugin;
		connect();
	}

	// Create instance during plugin load.
	public static SocketIOClient create(NodeBBIntegration plugin) {
		if (instance == null) instance = new SocketIOClient(plugin);
		return instance;
	}

	// Closes any previous connection and creates a new one.
	public static void connect() {
		new BukkitRunnable(){
			@Override
			public void run() {
				try {
					if (socket != null) socket.close();
					socket = IO.socket(ChatColor.stripColor(plugin.getConfig().getString("FORUMURL")));
					setOptions();
					socket.connect();
				} catch (URISyntaxException e) {
					NodeBBIntegration.log("The forum URL was invalid.");
				} catch (Exception e) {
					NodeBBIntegration.log("The forum URL was invalid.");
				}
			}
		}.runTaskLaterAsynchronously(plugin, 60);
	}

	public static boolean connected() {
		if (socket == null) return false;
		return socket.connected();
	}

	public static boolean disconnected() {
		return !connected();
	}

	public static void close() {
		socket.close();
	}

	private static void setOptions()
	{
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				NodeBBIntegration.log("Connected to the forum.");
				sync();
			}
		}).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args)
			{
				NodeBBIntegration.log("Connected to the forum.");
				sync();
			}
		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args)
			{
				NodeBBIntegration.log("Lost connection to the forum.");
			}
		}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
			@Override
			public void call(Object... objects) {
				NodeBBIntegration.log("Error connecting to the forum.");
			}
		});

		socket.on("eventWebChat", new ListenerWebChat());
		socket.on("eventGetPlayerVotes", new ListenerGetPlayerVotes());
	}

	public static void emit(String event, JSONObject args, Ack ack) {
		socket.emit(event, args, ack);
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

	// Runs a tick once to update the forum status.
	private static void sync() {
		id = socket.id();
		plugin.taskTick.run();
	}

	public static void sendPlayerJoin(Player player) {
		if (socket == null) return;
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
		socket.emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegration.log(socketEvent + " callback received.");
			}
		});
	}

	public static void sendPlayerLeave(Player player) {
		if (socket == null) return;
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
		socket.emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegration.log(socketEvent + " callback received.");
			}
		});
	}
}
