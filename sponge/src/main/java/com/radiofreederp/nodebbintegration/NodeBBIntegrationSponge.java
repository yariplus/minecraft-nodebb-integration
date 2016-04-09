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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Yari on 4/5/2016.
 */

@Plugin(id = "nodebbintegration", name = "NodeBBIntegration", version = "0.7.0-beta.12")
public class NodeBBIntegrationSponge implements NodeBBIntegrationPlugin {

    // Logger
    @Inject
    private Logger logger;
    @Inject
    public Logger getLogger() {
        return logger;
    }

    // Debug is initially true until the plugin is done loading.
    private boolean debug = true;
    @Override
    public boolean isDebug() {
        return debug;
    }
    @Override
    public void toggleDebug() {
        debug = !debug;
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

    private PluginConfig pluginConfig;
    private ConfigurationNode defaultConfig;
    private ConfigurationNode spongeConfig;

    @Override
    public PluginConfig getPluginConfig() {
        return this.pluginConfig;
    }
    public ConfigurationNode getDefaultConfig() {
        return this.defaultConfig;
    }
    public ConfigurationNode getSpongeConfig() {
        return this.spongeConfig;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
        return this.loader;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
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
                spongeConfig = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                spongeConfig = getDefaultConfig();
                loader.save(defaultConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getLogger().info(spongeConfig.getNode("version").getString());

        pluginConfig = new PluginConfigSponge(this);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
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
    public void eventWebChat(Object... args) {

    }

    @Override
    public void eventGetPlayerVotes(Object... args) {

    }
}
