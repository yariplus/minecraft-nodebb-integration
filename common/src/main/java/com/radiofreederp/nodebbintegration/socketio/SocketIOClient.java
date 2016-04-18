package com.radiofreederp.nodebbintegration.socketio;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
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
    public static void close() { if (hasSocket()) instance.socket.close(); }
    public static void emit(String event, JSONObject args, Ack ack) { if (connected()) instance.socket.emit(instance.namespace + event, args, ack); }

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
            plugin.doTaskTick();
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
                plugin.log("The forum URL was invalid.");
                e.printStackTrace();
            } catch (Exception e) {
                plugin.log("The forum URL was invalid.");
                e.printStackTrace();
            }
        });
    }

    // Get the express session cookie.
    private void getCookie(String _url) throws IOException {
        plugin.log("Getting Cookie.");

        URL url = new URL(_url);
        URLConnection connection = url.openConnection();

        try {
            connection.connect();
            cookie = connection.getHeaderField("Set-Cookie");
            plugin.log("Got Cookie: " + cookie);
        } catch (SSLHandshakeException e) {
            e.printStackTrace();
            plugin.log("Failed to find forum SSL certificates, you may need to add these manually.");
        }
    }

    // Add additional LE certificates.
    static {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Path ksPath = Paths.get(System.getProperty("java.home"), "lib", "security", "cacerts");

            keyStore.load(Files.newInputStream(ksPath), "changeit".toCharArray());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            try (InputStream caInput = new BufferedInputStream(NodeBBIntegrationPlugin.class.getResourceAsStream("/lets-encrypt-x1-cross-signed.der"))) {
                Certificate crt = cf.generateCertificate(caInput);
                keyStore.setCertificateEntry("lets-encrypt-x1-cross-signed", crt);
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            instance.plugin.log("Failed to load LE X1 certs.");
        }
    }
}
