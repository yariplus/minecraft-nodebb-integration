package com.radiofreederp.nodebbintegration;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.json.JSONException;
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
    return string;
  }

  @Override
  public String removeColors(String string) {
    return string;
  }

  @Override
  public String getTPS() {
    // TODO
    return "0";
  }

  @Override
  public ArrayList<JSONObject> getPlayerList() {
    return new ArrayList<JSONObject>();
  }

  @Override
  public ArrayList<JSONObject> getPluginList() {
    return new ArrayList<JSONObject>();
  }

  @Override
  public JSONObject getPlayerJSON(Object _player) {
    EntityPlayerMP player = (EntityPlayerMP)_player;
    JSONObject playerObj = new JSONObject();

    try {
      playerObj.put("name", player.getGameProfile().getName());
      playerObj.put("displayName", player.getDisplayName());
      playerObj.put("id", player.getGameProfile().getId().toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return playerObj;
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
    return "";
  }

  @Override
  public String getWorldType() {
    return "";
  }

  @Override
  public String getWorldName() {
    return "";
  }

  @Override
  public String getMotd() {
    return MinecraftServer.getServer().getMOTD();
  }

  @Override
  public String getPlayerPrefix(Object player) {
    return "";
  }

  @Override
  public int getOnlinePlayers() {
    return MinecraftServer.getServer().getCurrentPlayerCount();
  }

  @Override
  public int getMaxPlayers() {
    return MinecraftServer.getServer().getMaxPlayers();
  }

  @Override
  public JSONObject getGroups() {
    return new JSONObject();
  }

  @Override
  public JSONObject getGroupsWithMembers() {
    return new JSONObject();
  }

  @Override
  public JSONObject getOfflinePlayers() {
    return new JSONObject();
  }
}
