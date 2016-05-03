package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import com.radiofreederp.nodebbintegration.utils.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yari on 4/17/2016.
 */
public abstract class MinecraftServer implements IMinecraftServer {

    // Base methods are implemented by the actual server.
    abstract public void sendMessage(Object receiver, String message);
    abstract public void sendConsoleMessage(String message);

    abstract public String translateColors(String string);
    abstract public String removeColors(String string);

    // TODO: Replace parameters with a message interface.
    // Send single message.
    @Override
    public final void sendMessage(Object receiver, String message, HashMap<String, String> vars) {
        message = Helpers.replaceMap(message, vars);
        sendMessage(receiver, message);
    }
    @Override
    public final void sendConsoleMessage(String message, HashMap<String, String> vars) {
        message = Helpers.replaceMap(message, vars);
        sendConsoleMessage(message);
    }
    @Override
    public final void sendMessageToOps(String message, HashMap<String, String> vars) {
        message = Helpers.replaceMap(message, vars);
        sendMessageToOps(message);
    }

    // Send message list.
    @Override
    public final void sendMessage(Object receiver, List<String> messages) {
        messages.forEach(message->sendMessage(receiver, message));
    }
    @Override
    public final void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendMessage(receiver, message, vars));
    }
    @Override
    public final void sendConsoleMessage(List<String> messages) {
        messages.forEach(this::sendConsoleMessage);
    }
    @Override
    public final void sendConsoleMessage(List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendConsoleMessage(message, vars));
    }
    @Override
    public final void sendMessageToOps(List<String> messages) {
        messages.forEach(this::sendMessageToOps);
    }
    @Override
    public final void sendMessageToOps(List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendMessageToOps(message, vars));
    }
}
