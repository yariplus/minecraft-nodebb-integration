package com.radiofreederp.nodebbintegration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.*;
import java.util.logging.Level;

/**
 * Created by Yari on 4/5/2016.
 */
public interface NodeBBIntegrationPlugin {
    void log(String message);

    void log(String message, Level level);

    boolean isDebug();

    void toggleDebug();

    PluginConfig getPluginConfig();

    MinecraftServer getMinecraftServer();

    void runTaskAsynchronously(Runnable task);

    void runTaskTimerAsynchronously(Runnable task);

    void runTask(Runnable task);

    void eventWebChat(Object... args);

    void eventGetPlayerVotes(Object... args);

    void doTaskTick();
}
