package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.commands.CommandNodeBBSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.util.logging.Level;

/**
 * Created by Yari on 4/5/2016.
 */

@Plugin(id = "com.radiofreederp.nodebbintegration.NodeBBIntegration",
        name = "NodeBBIntegration",
        version = "0.7.0-beta.9")
public class NodeBBIntegrationSponge implements NodeBBIntegrationPlugin {
    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Register commands.
        CommandSpec specNodeBB = CommandSpec.builder()
            .description(Text.of("NodeBB Integration parent command."))
            .permission("nodebb.admin")
            .executor(new CommandNodeBBSponge())
            .build();
        CommandSpec specRegister = CommandSpec.builder()
            .description(Text.of("Register your Minecraft account with your forum account."))
            .executor(new CommandNodeBBSponge())
            .build();

        Sponge.getCommandManager().register(this, specNodeBB, "nodebb");
        Sponge.getCommandManager().register(this, specRegister, "register");
    }

    @Override
    public void log(String message) {

    }

    @Override
    public void log(String message, Level level) {

    }

    @Override
    public void runTaskAsynchronously(Runnable task) {

    }

    @Override
    public void runTask(Runnable task) {

    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public String getLive() {
        return null;
    }

    @Override
    public String[] getTransports() {
        return new String[0];
    }

    @Override
    public String getAPIKey() {
        return null;
    }

    @Override
    public void eventWebChat(Object... args) {

    }

    @Override
    public void eventGetPlayerVotes(Object... args) {

    }
}
