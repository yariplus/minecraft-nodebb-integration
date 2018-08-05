package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.forge.commands.CommandNodeBBForge;
import com.radiofreederp.nodebbintegration.forge.commands.CommandRegisterForge;
import com.radiofreederp.nodebbintegration.forge.configuration.PluginConfigForge;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.tasks.TaskPing;
import com.radiofreederp.nodebbintegration.tasks.TaskStatus;
import com.radiofreederp.nodebbintegration.utils.Logger;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Mod(modid = NBBPlugin.ID, name = NBBPlugin.NAME, version = NBBPlugin.VERSION, acceptableRemoteVersions = "*")
public class NodeBBIntegrationForge implements NodeBBIntegrationPlugin {
  private static NodeBBIntegrationPlugin plugin;
  public static NodeBBIntegrationPlugin getPlugin() {
    return plugin;
  }

  private MinecraftServerCommon minecraftServer = new ForgeServer(this);
  @Override
  public MinecraftServerCommon getMinecraftServer() {
    return minecraftServer;
  }

  // Preload event
  @Mod.EventHandler
  public void preinit(FMLPreInitializationEvent event) {
    NBBPlugin.instance = this;
    NodeBBIntegrationForge.plugin = this;

    new PluginConfigForge(event.getSuggestedConfigurationFile());

    Logger.init(this);
  }

  // Load event
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    // Start the socket client.
    SocketIOClient.create(this);

    // Schedule our repeating tasks.
    initTaskTick();

    Logger.log("Loaded NodeBB Integration");
  }

  // Server starting event
  @Mod.EventHandler
  public void starting(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandNodeBBForge(this));
    event.registerServerCommand(new CommandRegisterForge());
  }

  @Override
  // TODO
  public void log(String message, Level level) {
    FMLLog.getLogger().log(org.apache.logging.log4j.Level.toLevel(Logger.tol4j.get(level)), message);
  }

  @Override
  public void runTaskAsynchronously(Runnable task) {
    // TODO: I don't know what I'm doing.
    new Thread(task).run();
  }

  @Override
  public void runTaskTimerAsynchronously(final Runnable task, int delay, int interval) {
    // TODO: Please send help. Innocent children may die.
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    final Thread thread = new Thread(task);
    executor.scheduleAtFixedRate(thread, delay / 20, interval / 20, TimeUnit.SECONDS);
  }

  @Override
  public void runTask(Runnable task) {
    task.run();
  }

  @Override
  public void runTaskTimer(Runnable task, int delay, int interval) {
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    executor.scheduleAtFixedRate(task, delay / 20, interval / 20, TimeUnit.SECONDS);
  }

  @Override
  public void initTaskTick() {
    new TaskStatus(this);
    new TaskPing(this);
  }

  @Override
  public void eventWebChat(Object... args) {
    // Interpret message.
    if (args[0] != null)
    {
      try
      {
        final String name = ((JSONObject)args[0]).getString("name");
        final String message = ((JSONObject)args[0]).getString("message");
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void eventGetPlayerVotes(Object... args) {

  }
}
