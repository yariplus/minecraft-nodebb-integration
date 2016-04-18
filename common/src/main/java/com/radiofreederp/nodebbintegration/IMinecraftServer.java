package com.radiofreederp.nodebbintegration;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yari on 4/17/2016.
 */
public interface IMinecraftServer {
    void sendMessage(Object receiver, String message);
    void sendConsoleMessage(String message);

    abstract public String translateColors(String string);
    abstract public String removeColors(String string);

    void sendMessage(Object receiver, String message, HashMap<String, String> vars);
    void sendConsoleMessage(String message, HashMap<String, String> vars);
    void sendMessage(Object receiver, List<String> messages);
    void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars);
    void sendConsoleMessage(List<String> messages);
    void sendConsoleMessage(List<String> messages, HashMap<String, String> vars);
}
