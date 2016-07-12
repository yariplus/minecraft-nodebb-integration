package com.radiofreederp.nodebbintegration;

import com.radiofreederp.nodebbintegration.tasks.TaskTick;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

@Mod(modid = NodeBBIntegrationForge.MODID, version = NodeBBIntegrationForge.VERSION, acceptableRemoteVersions = "*")
public class NodeBBIntegrationForge implements NodeBBIntegrationPlugin {
  public static final String MODID = "examplemod";
  public static final String VERSION = "1.0";

  private boolean debug = false;
  private Logger logger = FMLLog.getLogger();

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {

  }

  @Override
  public void log(String message) {
    logger.info(message);
  }

  @Override
  // TODO
  public void log(String message, java.util.logging.Level level) {
    logger.log(Level.INFO, message);
  }

  @Override
  public void error(String message) {
    logger.error(message);
  }

  @Override
  public boolean isDebug() {
    return debug;
  }

  @Override
  public void toggleDebug() {
    debug = !debug;
  }

  @Override
  public PluginConfig getPluginConfig() {
    return null;
  }

  @Override
  public MinecraftServerCommon getMinecraftServer() {
    return null;
  }

  @Override
  public void runTaskAsynchronously(Runnable task) {
    // TODO: I don't know what I'm doing.
    new Thread(task).run();
  }

  @Override
  public void runTaskTimerAsynchronously(Runnable task) {
    // TODO: Count ticks?
  }

  @Override
  public void runTask(Runnable task) {
    task.run();
  }

  @Override
  public void initTaskTick() {
    // TODO: Maybe I could start a timer here?
    new TaskTick(this);
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
