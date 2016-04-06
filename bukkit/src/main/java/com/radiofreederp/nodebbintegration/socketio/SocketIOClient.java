package com.radiofreederp.nodebbintegration.socketio;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.hooks.VaultHook;
import com.radiofreederp.nodebbintegration.hooks.OnTimeHook;
import com.radiofreederp.nodebbintegration.hooks.VanishNoPacketHook;
import com.radiofreederp.nodebbintegration.socketio.listeners.ListenerGetPlayerVotes;
import com.radiofreederp.nodebbintegration.socketio.listeners.ListenerWebChat;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SocketIOClient {

	// Singleton, for now.
	private static SocketIOClient instance;

	private OkHttpClient client;
	private Socket socket;
	private NodeBBIntegrationBukkit plugin;

	private String cookie;
	private String url;
	private String namespace;

	// Create instance during plugin load.
	public static SocketIOClient create(NodeBBIntegrationBukkit plugin) {
		if (instance == null) instance = new SocketIOClient(plugin);
		return instance;
	}

	// Initial connection when created.
	private SocketIOClient(NodeBBIntegrationBukkit _plugin) {
		plugin = _plugin;
		client = new OkHttpClient();
		connectSocket();
	}

	// Get the express session cookie.
	private void getCookie() throws IOException {
		NodeBBIntegrationBukkit.log("Getting Cookie.");
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		cookie = response.headers().get("Set-Cookie");
	}

	// Setup Socket Events
	private void setupSocket() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				NodeBBIntegrationBukkit.log("Connected to the forum.");
				plugin.taskTick.run();
			}
		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				NodeBBIntegrationBukkit.log("Lost connection to the forum.");
				NodeBBIntegrationBukkit.log(args[0].toString());
			}
		}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
			@Override
			public void call(Object... objects) {
				NodeBBIntegrationBukkit.log("Error connecting to the forum.");
				NodeBBIntegrationBukkit.log(objects[0].toString());
			}
		});

		socket.on("eventWebChat", new ListenerWebChat());
		socket.on("eventGetPlayerVotes", new ListenerGetPlayerVotes());
	}

	private static boolean hasSocket() {
		return instance != null && instance.socket != null;
	}

	// Disconnect the socket and reconnect asynchronously.
	private void connectSocket() {
		NodeBBIntegrationBukkit.log("Reconnecting socket...");
		new BukkitRunnable(){
			@Override
			public void run() {
				try {
					// Close previous sockets, and get the forum url and namespace.
					if (socket != null) socket.close();
					url = ChatColor.stripColor(plugin.getConfig().getString("FORUMURL"));
					namespace = instance.plugin.getConfig().getString("SOCKETNAMESPACE") + "." + instance.plugin.getConfig().getString("PLUGINID") + ".";

					// Get a session cookie.
					getCookie();
					NodeBBIntegrationBukkit.log("Got Cookie: " + cookie);

					// Get config.
					String live = plugin.getConfig().getString("socketio.address");
					if (live.equals(plugin.getConfig().getDefaults().getString("socketio.address"))) live = plugin.getConfig().getString("FORUMURL");
					String[] transports = plugin.getConfig().getStringList("socketio.transports").toArray(new String[0]);
					IO.Options options = new IO.Options();
					options.transports = transports;

					// Create a new socket.
					socket = IO.socket(live, options);

					// Send the session cookie with requests.
					socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							Transport transport = (Transport)args[0];

							transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
								@Override
								public void call(Object... args) {
									@SuppressWarnings("unchecked")
									Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
									headers.put("Cookie", Arrays.asList(cookie));
								}
							});
						}
					});

					// Setup events and such.
					setupSocket();

					// Connect to the forum.
					socket.connect();

				} catch (URISyntaxException e) {
					NodeBBIntegrationBukkit.log("The forum URL was invalid.");
					e.printStackTrace();
				} catch (Exception e) {
					NodeBBIntegrationBukkit.log("The forum URL was invalid.");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	public static void connect() {
		instance.connectSocket();
	}

	public static boolean connected() {
		if (!hasSocket()) return false;
		return instance.socket.connected();
	}

	public static boolean disconnected() {
		return !connected();
	}

	public static void close() {
		if (hasSocket()) instance.socket.close();
	}

	public static void emit(String event, JSONObject args, Ack ack) {
		if (connected()) instance.socket.emit(instance.namespace + event, args, ack);
	}

	public static void sendPlayerJoin(Player player) {
		if (disconnected()) return;
		final String socketEvent = "eventPlayerJoin";

		if (VanishNoPacketHook.isEnabled()) {
			if (VanishNoPacketHook.isVanished(player.getName())) return;
		}

		JSONObject obj = new JSONObject();
		try {
			obj.put("name", player.getName());
			obj.put("id", player.getUniqueId());
			obj.put("key", instance.plugin.getConfig().getString("APIKEY"));

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
			NodeBBIntegrationBukkit.log("Error constructing JSON Object for " + socketEvent);
			e.printStackTrace();
			return;
		}

		NodeBBIntegrationBukkit.log("Sending " + socketEvent);
		instance.emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegrationBukkit.log(socketEvent + " callback received.");
			}
		});
	}

	public static void sendPlayerLeave(Player player) {
		if (disconnected()) return;
		final String socketEvent = "eventPlayerQuit";

		JSONObject obj = new JSONObject();
		try {
			obj.put("name", player.getName());
			obj.put("id", player.getUniqueId());
			obj.put("key", instance.plugin.getConfig().getString("APIKEY"));
		} catch (JSONException e) {
			NodeBBIntegrationBukkit.log("Error constructing JSON Object for " + socketEvent);
			e.printStackTrace();
			return;
		}

		NodeBBIntegrationBukkit.log("Sending " + socketEvent);
		instance.emit(socketEvent, obj, new Ack() {
			@Override
			public void call(Object... args) {
				NodeBBIntegrationBukkit.log(socketEvent + " callback received.");
			}
		});
	}
}
