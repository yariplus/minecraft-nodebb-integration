package com.radiofreederp.nodebbintegration;

import com.google.inject.Inject;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBBSponge;
import com.radiofreederp.nodebbintegration.commands.CommandRegisterSponge;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigManager;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by Yari on 4/5/2016.
 */

@Plugin(id = "nodebbintegration", name = "NodeBBIntegration", version = "0.7.0-beta.11")
public class NodeBBIntegrationSponge implements NodeBBIntegrationPlugin {

    @Inject
    private Logger logger;
    @Inject
    public Logger getLogger() {
        return logger;
    }

    @Inject
    public NodeBBIntegrationSponge(Logger logger) {
        this.logger = logger;
    }

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File defaultConfigFile;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private YAMLConfigurationLoader jarLoader;

    private ConfigurationNode defaultConfig;
    private ConfigurationNode config;

    public ConfigurationNode getConfig() {
        return this.config;
    }
    public ConfigurationNode getDefaultConfig() {
        return this.defaultConfig;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
        return this.loader;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        getLogger().info("Starting NodeBB Integration.");

        jarLoader = YAMLConfigurationLoader.builder().setURL(this.getClass().getResource("/config.yml")).build();

        try {
            defaultConfig = jarLoader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        loader = HoconConfigurationLoader.builder().setPath(defaultConfigFile.toPath()).build();
        if (defaultConfigFile.exists()) {
            try {
                config = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                config = defaultConfig;
                loader.save(defaultConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getLogger().info(config.getNode("version").getString());

        // Register commands.
        CommandSpec specNodeBB = CommandSpec.builder()
                .description(Text.of("NodeBB Integration parent command."))
                .permission("nodebb.admin")
                .executor(new CommandNodeBBSponge(this))
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("command")), Text.of("help")),
                        GenericArguments.optional(GenericArguments.string(Text.of("value")))
                )
                .build();
        CommandSpec specRegister = CommandSpec.builder()
                .description(Text.of("Register your Minecraft account with your forum account."))
                .executor(new CommandRegisterSponge())
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
