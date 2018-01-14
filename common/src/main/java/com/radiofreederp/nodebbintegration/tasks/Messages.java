package com.radiofreederp.nodebbintegration.tasks;

import java.util.ArrayList;

public final class Messages {
	private Messages () {}

	public static String SOCKETIO_DOMAIN_GET = "The websocket domain is set to %live%";
	public static String SOCKETIO_DOMAIN_SET = "Set the websocket domain to %live%";

	public static String SOCKETIO_TRANSPORTS_GET = "Websocket transports are %transports%";
	public static String SOCKETIO_TRANSPORTS_ADD = "Added websocket transport %transport%";
	public static String SOCKETIO_TRANSPORTS_REM = "Removed websocket transport %transport%";

	public static String SOCKETIO_NAMESPACE_GET = "Websocket namespace is %namespace%";
	public static String SOCKETIO_NAMESPACE_SET = "Websocket namespace is %namespace%";

	public static String[] HELP = {
	  "&f[&6NodeBB Integration&f]",
	  "&4=&c=&6=&e=&a=&3=&b=&9=&1=&d=&5=&4=&c=&6=&e=&a=&2=&3=&b=",
	  "&fCommands:",
	  "&e/nodebb reload &r- Reloads config and reconnects to the forum.",
	  "&e/nodebb key [key] &r- Get or set the forum API key.",
	  "&e/nodebb name [name] &r- Get or set the forum name.",
	  "&e/nodebb url [url] &r- Get or set the forum url.",
	  "&e/nodebb debug &r- Displays information useful for fixing errors."
	};

	public static String LINK = "Go to the link below to link your player to the forum.";
	public static String REGISTER = "Registering your player on %forumname% (%forumurl%&r)";
	public static String DISCONNECTED = "Sorry! The server isn't currently connected to the forum.";

	public static String[] HELP_REGISTER = {
	  "To register as a new forum user,",
	  "use the command /register [username] [email]"
	};

	public static String REGISTER_SUCCESS = "Success! Created a new user on %forumname%";

	public static String FAIL_DB = "Error on forum. Please inform an administrator.";
	public static String FAIL_RES = "Internal error. Please inform an administrator.";
	public static String FAIL_DATA = "Data error. Please inform an administrator.";
	public static String FAIL_PARSE = "Parsing error. Please inform an administrator.";
}
