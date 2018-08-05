package com.radiofreederp.nodebbintegration;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class ForgeServer extends MinecraftServerCommon {
  private NodeBBIntegrationPlugin plugin;

  public ForgeServer(NodeBBIntegrationPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void sendMessage(Object receiver, String message) {
    message = message.replaceAll("&", "\u00a7");
    ((EntityPlayerMP)receiver).addChatMessage(new ChatComponentText(message));
  }

  @Override
  public void sendConsoleMessage(String message) {

  }

  @Override
  public void sendMessageToOps(final String message) {
	  final String fmessage = message.replaceAll("&", "\u00a7");
	  final String[] ops = MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames();

	  MinecraftServer.getServer().getConfigurationManager().playerEntityList.forEach(new Consumer() {
		  @Override
		  public void accept(Object o) {
			  EntityPlayerMP player = (EntityPlayerMP) o;
			  if (Arrays.asList(ops).contains(player.getCommandSenderName())) {
				  player.addChatMessage(new ChatComponentText(fmessage));
			  }
		  }
	  });
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

  @Override
  public JSONArray getScoreboards() {
    return null;
  }
}
