package com.radiofreederp.nodebbintegration;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yari on 7/9/2016.
 */
public class ForgeServer extends MinecraftServerCommon {
  private NodeBBIntegrationPlugin plugin;

  public ForgeServer(NodeBBIntegrationPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void sendMessage(Object receiver, String message) {
    ((EntityPlayerMP)receiver).addChatMessage(new ChatComponentText(message));
  }

  @Override
  public void sendConsoleMessage(String message) {

  }

  @Override
  public void sendMessageToOps(String message) {

  }

  @Override
  public String translateColors(String string) {
    return null;
  }

  @Override
  public String removeColors(String string) {
    return null;
  }

  @Override
  public String getTPS() {
    // TODO
    return "0";
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
    return MinecraftServer.getServer().getMinecraftVersion();
  }

  @Override
  public String getServerName() {
    return MinecraftServer.getServer().getServerModName();
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
    return MinecraftServer.getServer().getMOTD();
  }

  @Override
  public int getOnlinePlayers() {
    return MinecraftServer.getServer().getCurrentPlayerCount();
  }

  @Override
  public int getMaxPlayers() {
    return MinecraftServer.getServer().getMaxPlayers();
  }
}
