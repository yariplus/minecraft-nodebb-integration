package com.radiofreederp.nodebbintegration;

import com.flowpowered.noise.module.combiner.Min;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yari on 4/17/2016.
 */
public class SpongeServer implements MinecraftServer {

    private final NodeBBIntegrationPlugin plugin;
    public SpongeServer(NodeBBIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(Object receiver, String message) {

    }

    @Override
    public void sendMessage(Object receiver, String message, HashMap<String, String> vars) {

    }

    @Override
    public void sendMessage(Object receiver, List<String> messages) {

    }

    @Override
    public void sendMessage(Object receiver, List<String> messages, HashMap<String, String> vars) {

    }

    @Override
    public void sendConsoleMessage(String message) {

    }

    @Override
    public void sendConsoleMessage(String message, HashMap<String, String> vars) {

    }

    @Override
    public void sendConsoleMessage(List<String> messages) {

    }

    @Override
    public void sendConsoleMessage(List<String> messages, HashMap<String, String> vars) {

    }

    @Override
    public String translateColors(String string) {
        return null;
    }

    @Override
    public String removeColors(String string) {
        return null;
    }
}
