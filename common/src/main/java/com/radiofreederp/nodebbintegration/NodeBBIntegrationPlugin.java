package com.radiofreederp.nodebbintegration;

import java.util.logging.Level;

public interface NodeBBIntegrationPlugin {
    // TODO: This can be static.
    MinecraftServerCommon getMinecraftServer();

    void log(String message, Level level);

    void runTaskAsynchronously(Runnable task);
    void runTaskTimerAsynchronously(Runnable task);
    void runTask(Runnable task);

    void initTaskTick();

    // TODO
    void eventWebChat(Object... args);
    void eventGetPlayerVotes(Object... args);
}
