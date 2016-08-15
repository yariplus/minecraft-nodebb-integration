package com.radiofreederp.nodebbintegration.socketio;

/**
 * Created by Yari on 8/12/2016.
 */
public interface ESocketEvent {
  String SEND_SERVER_STATUS = "eventStatus";
  String SEND_OFFLINE_PLAYERS = "eventOfflinePlayers";
  String SEND_GROUPS = "";
  String SEND_PLAYER_LOGOUT = "";
  String SEND_PLAYER_LOGIN = "";
  String SEND_PLAYER_CHAT = "";
}
