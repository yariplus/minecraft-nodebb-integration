package com.radiofreederp.nodebbintegration.socketio;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class SocketIOClient {

    // Singleton, for now.
    private static SocketIOClient instance;

    private Socket socket;
    private NodeBBIntegrationPlugin plugin;
    private String live;
    private List<String> transports;
    private String cookie;
    private String url;
    private String namespace;

    private static boolean hasSocket() {
        return instance != null && instance.socket != null;
    }

    public static void connect() { instance.connectSocket(); }
    public static boolean connected() { return hasSocket() && instance.socket.connected(); }
    public static boolean disconnected() { return !connected(); }
    public static void close() {
        if (hasSocket()) {
            try {
                instance.socket.close();
            } catch (Exception ignored) {}
        }
    }

    public static void emit(final String event, final JSONObject data, final Ack ack) {
        if (connected()) instance.socket.emit(instance.namespace + event, data, new Ack() {
            @Override
            public void call(Object... args) {
                if (args[0] == null) {
                    instance.plugin.log(event + " callback received without error.");
                } else {
                    try {
                        instance.plugin.log(event + " callback received with error: " + ((JSONObject)args[0]).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ack.call(args);
            }
        });
    }

    public interface Events {
        String onPlayerJoin = "eventPlayerJoin";
        String onPlayerQuit = "eventPlayerQuit";
    }

    // Create instance during plugin load.
    public static SocketIOClient create(NodeBBIntegrationPlugin plugin) {
        if (instance == null) instance = new SocketIOClient(plugin);
        return instance;
    }

    // Initial connection when created.
    private SocketIOClient(NodeBBIntegrationPlugin _plugin) {
        plugin = _plugin;

        connectSocket();
    }

    // Setup Socket Events
    private void setupSocket() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            plugin.log("Connected to the forum.");
            plugin.getMinecraftServer().sendMessageToOps("Connected to the forum.");
            plugin.runTask(TaskTick.getTask());
        }).on(Socket.EVENT_DISCONNECT, args -> {
            plugin.log("Lost connection to the forum.");
            plugin.getMinecraftServer().sendMessageToOps("Lost connection to the forum.");
            plugin.log(args[0].toString());
        }).on(Socket.EVENT_CONNECT_ERROR, args -> {
            plugin.log("Error connecting to the forum.");
            plugin.log(args[0].toString());
        });

        socket.on("eventWebChat", args -> plugin.eventWebChat(args));
        socket.on("eventGetPlayerVotes", args -> plugin.eventGetPlayerVotes(args));
    }

    // Disconnect the socket and reconnect asynchronously.
    private void connectSocket() {
        plugin.log("Reconnecting socket...");
        plugin.runTaskAsynchronously(() -> {
            try {
                // Close previous sockets, and get the forum url and namespace.
                if (socket != null) socket.close();

                // Get config.
                live = plugin.getMinecraftServer().removeColors(plugin.getPluginConfig().getSocketAddress());
                transports = plugin.getPluginConfig().getSocketTransports();
                url = plugin.getMinecraftServer().removeColors(plugin.getPluginConfig().getForumURL());
                namespace = plugin.getMinecraftServer().removeColors(plugin.getPluginConfig().getSocketNamespace());

                // ID-10T checks.
                if (url.length() > 10) {
                    if (url.charAt(url.length() - 1) != '/') url = url + "/";
                    if (!url.substring(0, 4).equals("http")) url = "http://" + url;
                }

                // Get a session cookie.
                getCookie(url);
                if (cookie == null) return;

                // Set SocketIO options.
                IO.Options options = new IO.Options();
                options.transports = transports.toArray(new String[transports.size()]);

                // Create a new socket.
                socket = IO.socket(live, options);

                // Send the session cookie with requests.
                socket.io().on(Manager.EVENT_TRANSPORT, args -> {
                    Transport transport = (Transport)args[0];

                    transport.on(Transport.EVENT_REQUEST_HEADERS, args2 -> {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>)args2[0];
                        headers.put("Cookie", Arrays.asList(cookie));
                    });
                });

                // Setup events and such.
                setupSocket();

                // Connect to the forum.
                socket.connect();

            } catch (URISyntaxException e) {
                plugin.error("The forum URL is incorrectly formatted.");
                if (plugin.isDebug()) e.printStackTrace();
            } catch (IOException e) {
                plugin.error("The forum URL is invalid.");
                if (plugin.isDebug()) e.printStackTrace();
            }
        });
    }

    // Get the express session cookie.
    private void getCookie(String _url) throws IOException {
        plugin.log("Getting Session Cookie.");

        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.connect();
            cookie = connection.getHeaderField("Set-Cookie");
            int response = connection.getResponseCode();

            if (response/100 == 4) {
                plugin.error("Forum returned a " + response + " Forbidden error, you may need to whitelist your server's address on your forum's firewall.");
                cookie = null;
            }
            if (response/100 == 3) {
                plugin.error("Forum returned a " + response + " Redirect error, please use the actual forum address.");
                cookie = null;
            }

            plugin.log("Got Cookie: " + cookie);
        } catch (SSLHandshakeException e) {
            plugin.error("Failed to verify SSL certificates from your forum, you may need to add these manually.");
            if (plugin.isDebug()) e.printStackTrace();
            cookie = null;
        } catch (UnknownHostException e) {
            plugin.error("Can't connect to forum at " + _url);
            plugin.error("Use `/nodebb url URL` to set the forum address.");
            cookie = null;
        }
    }
}
