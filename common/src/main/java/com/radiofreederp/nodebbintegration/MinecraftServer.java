package com.radiofreederp.nodebbintegration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yari on 4/17/2016.
 */
public abstract class MinecraftServer implements IMinecraftServer {

    abstract public void sendMessage(Object receiver, String message);
    abstract public void sendConsoleMessage(String message);

    abstract public String translateColors(String string);
    abstract public String removeColors(String string);

    // Send single message.
    public final void sendMessage(Object receiver, String message, HashMap<String, String> vars) {
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            message = message.replaceAll(entry.getKey(), entry.getValue());
        }
        sendMessage(receiver, message);
    }
    public final void sendConsoleMessage(String message, HashMap<String, String> vars) {
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            message = message.replaceAll(entry.getKey(), entry.getValue());
        }
        sendConsoleMessage(message);
    }

    // Send message list.
    public final void sendMessage(Object receiver, List<String> messages) {
        messages.forEach(message->sendMessage(receiver, message));
    }
    public final void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendMessage(receiver, message, vars));
    }
    public final void sendConsoleMessage(List<String> messages) {
        messages.forEach(message->sendConsoleMessage(message));
    }
    public final void sendConsoleMessage(List<String> messages, HashMap<String, String> vars) {
        messages.forEach(message->sendConsoleMessage(message, vars));
    }
}
