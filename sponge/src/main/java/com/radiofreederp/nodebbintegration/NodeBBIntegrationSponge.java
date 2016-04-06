package com.radiofreederp.nodebbintegration;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * Created by Yari on 4/5/2016.
 */

@Plugin(id = "com.radiofreederp.nodebbintegration.NodeBBIntegration",
        name = "NodeBBIntegration",
        version = "0.7.0-beta.9")
public class NodeBBIntegrationSponge implements NodeBBIntegrationPlugin {
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
