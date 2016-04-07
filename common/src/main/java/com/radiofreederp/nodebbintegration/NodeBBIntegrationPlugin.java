package com.radiofreederp.nodebbintegration;

import org.json.JSONObject;

import java.util.logging.Level;

/**
 * Created by Yari on 4/5/2016.
 */
public interface NodeBBIntegrationPlugin {
    void log(String message);
    void log(String message, Level level);

    void runTaskAsynchronously(Runnable task);
    void runTask(Runnable task);

    // TODO: Better config setup.
    String getUrl();
    String getNamespace();
    String getLive();
    String[] getTransports();
    String getAPIKey();

    void eventWebChat(Object... args);
    void eventGetPlayerVotes(Object... args);
}
