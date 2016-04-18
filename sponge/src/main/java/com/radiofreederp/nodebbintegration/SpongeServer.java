package com.radiofreederp.nodebbintegration;

import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;

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
}
