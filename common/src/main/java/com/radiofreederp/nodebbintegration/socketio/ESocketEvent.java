package com.radiofreederp.nodebbintegration.socketio;

public interface ESocketEvent {
	String SERVER_STATUS = "status";
	String SERVER_PING = "ping";
	String OFFLINE_PLAYERS = "offlinePlayers";
	String WRITE_RANKS = "ranks";
	String RANKS_WITH_MEMBERS = "ranksWithMembers";
	String PLAYER_JOIN = "join";
	String PLAYER_QUIT = "quit";
	String PLAYER_CHAT = "playerChat";
}
