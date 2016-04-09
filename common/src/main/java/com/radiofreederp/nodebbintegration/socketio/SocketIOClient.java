package com.radiofreederp.nodebbintegration.socketio;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class SocketIOClient {

    // Singleton, for now.
    private static SocketIOClient instance;

    private OkHttpClient client;
    private Socket socket;

    protected NodeBBIntegrationPlugin plugin;

    private String live;
    private String[] transports;
    private String cookie;
    private String url;
    private String namespace;

    // Create instance during plugin load.
    public static SocketIOClient create(NodeBBIntegrationPlugin plugin) {
        if (instance == null) instance = new SocketIOClient(plugin);
        return instance;
    }

    // Initial connection when created.
    private SocketIOClient(NodeBBIntegrationPlugin _plugin) {
        plugin = _plugin;
        client = new OkHttpClient();
        connectSocket();
    }

    // Get the express session cookie.
    private void getCookie() throws IOException {
        plugin.log("Getting Cookie.");
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        cookie = response.headers().get("Set-Cookie");
    }

    // Setup Socket Events
    private void setupSocket() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            plugin.log("Connected to the forum.");
            //plugin.taskTick.run();
        }).on(Socket.EVENT_DISCONNECT, args -> {
            plugin.log("Lost connection to the forum.");
            plugin.log(args[0].toString());
        }).on(Socket.EVENT_CONNECT_ERROR, args -> {
            plugin.log("Error connecting to the forum.");
            plugin.log(args[0].toString());
        });

        socket.on("eventWebChat", args -> plugin.eventWebChat(args));
        socket.on("eventGetPlayerVotes", args -> plugin.eventGetPlayerVotes(args));
    }

    private static boolean hasSocket() {
        return instance != null && instance.socket != null;
    }

    // Disconnect the socket and reconnect asynchronously.
    private void connectSocket() {
        plugin.log("Reconnecting socket...");
        plugin.runTaskAsynchronously(() -> {
            try {
                // Close previous sockets, and get the forum url and namespace.
                if (socket != null) socket.close();

                // Get config.
                live = plugin.getPluginConfig().getSocketAddress();
                transports = plugin.getPluginConfig().getSocketTransports();
                url = plugin.getPluginConfig().getForumURL();
                namespace = plugin.getPluginConfig().getSocketNamespace();

                // Get a session cookie.
                getCookie();
                plugin.log("Got Cookie: " + cookie);

                // Set SocketIO options.
                IO.Options options = new IO.Options();
                options.transports = transports;

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
                plugin.log("The forum URL was invalid.");
                e.printStackTrace();
            } catch (Exception e) {
                plugin.log("The forum URL was invalid.");
                e.printStackTrace();
            }
        });
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

    public interface Events {
        String onPlayerJoin = "eventPlayerJoin";
        String onPlayerQuit = "eventPlayerQuit";
    }
}
