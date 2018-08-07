package com.radiofreederp.nodebbintegration;

import com.google.inject.Inject;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.sponge.commands.CommandLinkSponge;
import com.radiofreederp.nodebbintegration.sponge.commands.CommandNodeBBSponge;
import com.radiofreederp.nodebbintegration.sponge.commands.CommandRegisterSponge;
import com.radiofreederp.nodebbintegration.sponge.configuration.PluginConfigSponge;
import com.radiofreederp.nodebbintegration.sponge.listeners.ListenerNodeBBIntegration;
import com.radiofreederp.nodebbintegration.tasks.TaskPing;
import com.radiofreederp.nodebbintegration.tasks.TaskStatus;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.logging.Level;

@Plugin(id = NBBPlugin.ID, name = NBBPlugin.NAME, version = NBBPlugin.VERSION)
public class NodeBBIntegrationSponge implements NodeBBIntegrationPlugin {

    private final SpongeServer server = new SpongeServer(this);
    private final Task.Builder taskBuilder = Task.builder();

    // Logger
    @Inject
    private Logger logger;
    @Inject
    public Logger getLogger() {
        return logger;
    }
    @Override
    public void log(String message, Level level) {
        logger.info(message);
    }

    @Inject
    public NodeBBIntegrationSponge(Logger logger) {
        this.logger = logger;
    }

    @Inject
    private PluginContainer pluginContainer;

    // Injects private dir path for config files.
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    @Override
    public MinecraftServerCommon getMinecraftServer() {
        return server;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        getLogger().info("Starting NodeBB Integration.");

		new PluginConfigSponge(privateConfigDir);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Set static instance.
        NBBPlugin.instance = this;

        // Start logger.
        com.radiofreederp.nodebbintegration.utils.Logger.init(this);

        // Start the socket client.
        SocketIOClient.create(this);

        // Init tick task.
        initTaskTick();

        // Register listeners.
        Sponge.getEventManager().registerListeners(this, new ListenerNodeBBIntegration());

        // Register commands.
        CommandSpec specNodeBB = CommandSpec.builder()
                .description(Text.of("NodeBB Integration parent command."))
                .permission("nodebb.admin")
                .executor(new CommandNodeBBSponge())
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("option"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("value"))),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("remaining")))
                )
                .build();
        CommandSpec specRegister = CommandSpec.builder()
          .description(Text.of("Create a new forum account."))
          .executor(new CommandRegisterSponge())
          .arguments(
            GenericArguments.optional(GenericArguments.string(Text.of("username"))),
            GenericArguments.optional(GenericArguments.string(Text.of("email")))
          )
          .build();
        CommandSpec specLink = CommandSpec.builder()
          .description(Text.of("Register your Minecraft account with your forum account."))
          .executor(new CommandLinkSponge())
          .build();
        Sponge.getCommandManager().register(this, specNodeBB, "nodebb");
        Sponge.getCommandManager().register(this, specLink, "register");
        Sponge.getCommandManager().register(this, specLink, "link");
    }

    @Override
    public void runTaskAsynchronously(Runnable task) {
        taskBuilder.execute(task).async().submit(this);
    }

    @Override
    public void runTaskTimerAsynchronously(Runnable task, int delay, int interval) {
        taskBuilder.execute(task).async().delayTicks(delay).intervalTicks(interval).submit(this);
    }

    @Override
    public void runTask(Runnable task) {
        taskBuilder.execute(task).submit(this);
    }

    @Override
    public void runTaskTimer(Runnable task, int delay, int interval) {
        taskBuilder.execute(task).delayTicks(delay).intervalTicks(interval).submit(this);
    }

    @Override
    public void initTaskTick() {
        new TaskStatus(this);
        new TaskPing(this);
    }

    @Override
    public void eventWebChat(Object... args) {

    }

    @Override
    public void eventGetPlayerVotes(Object... args) {

    }
}
