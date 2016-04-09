package com.radiofreederp.nodebbintegration;

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

    void runTaskAsynchronously(Runnable task);
    void runTask(Runnable task);

    void eventWebChat(Object... args);
    void eventGetPlayerVotes(Object... args);
}
