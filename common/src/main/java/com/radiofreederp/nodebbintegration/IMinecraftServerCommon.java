package com.radiofreederp.nodebbintegration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yari on 4/17/2016.
 */
public interface IMinecraftServerCommon {

    // TODO: I feel like this can be improved a lot.
    void sendMessage(Object receiver, String message);
    void sendMessage(Object receiver, String message, HashMap<String, String> vars);
    void sendMessage(Object receiver, List<String> messages);
    void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars);

    void sendConsoleMessage(String message);
    void sendConsoleMessage(String message, HashMap<String, String> vars);
    void sendConsoleMessage(List<String> messages);
    void sendConsoleMessage(List<String> messages, HashMap<String, String> vars);

    void sendMessageToOps(String message);
    void sendMessageToOps(String message, HashMap<String, String> vars);
    void sendMessageToOps(List<String> messages);
    void sendMessageToOps(List<String> messages, HashMap<String, String> vars);

    String translateColors(String string);
    String removeColors(String string);

    String getTPS();

    ArrayList<JSONObject> getPlayerList();
    ArrayList<JSONObject> getPluginList();

    String getVersion();
    String getServerName();

    String getServerIcon();

    String getWorldType();
    String getWorldName();

    String getMotd();

    int getOnlinePlayers();
    int getMaxPlayers();

    JSONObject getGroups();
    JSONObject getGroupsWithMembers();
    JSONObject getOfflinePlayers();
}
