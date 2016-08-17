package com.radiofreederp.nodebbintegration.socketio;

/**
 * Created by Yari on 8/12/2016.
 */
public interface ESocketEvent {
  String SEND_SERVER_STATUS = "eventStatus";
  String WRITE_OFFLINE_PLAYERS = "writeOfflinePlayers";
  String WRITE_RANKS = "writeRanks";
  String WRITE_RANKS_WITH_MEMBERS = "writeRanksWithMembers";
  String SEND_PLAYER_LOGOUT = "";
  String SEND_PLAYER_LOGIN = "";
  String SEND_PLAYER_CHAT = "";
}
