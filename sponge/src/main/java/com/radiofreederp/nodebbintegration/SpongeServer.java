package com.radiofreederp.nodebbintegration;

import org.json.JSONObject;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;

/**
 * Created by Yari on 4/17/2016.
 */
public class SpongeServer extends MinecraftServer {

    private final NodeBBIntegrationPlugin plugin;
    public SpongeServer(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    // Handle messaging.
    @Override
    public void sendMessage(Object receiver, String message) {
        ((MessageReceiver)receiver).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }
    @Override
    public void sendConsoleMessage(String message) {

    }

    // Handle colors.
    @Override
    public String translateColors(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string).toString();
    }

    @Override
    public String removeColors(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string).toPlain();
    }

    @Override
    public ArrayList<JSONObject> getPlayerList() {
        return null;
    }

    @Override
    public ArrayList<JSONObject> getPluginList() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public String getServerIcon() {
        return null;
    }

    @Override
    public String getWorldType() {
        return null;
    }

    @Override
    public String getWorldName() {
        return null;
    }

    @Override
    public String getMotd() {
        return null;
    }

    @Override
    public int getOnlinePlayers() {
        return 0;
    }

    @Override
    public int getMaxPlayers() {
        return 0;
    }
}
