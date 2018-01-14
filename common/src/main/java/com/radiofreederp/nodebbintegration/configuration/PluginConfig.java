package com.radiofreederp.nodebbintegration.configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public abstract class PluginConfig implements IPluginConfig {
	public PluginConfig(Object... args) {
		instance = this;
		instance.init(args);
	}

	public static PluginConfig instance;

	// Config variables and defaults.
	protected static String defForumUrl = "https://forum.example.com/";
	protected static String defForumName = "Example Forum";
	protected static String defForumAPIKey = "SECRETPASSWORD";

	protected static String defSocketAddress = "https://forum.example.com/";
	protected static List<String> defSocketTransports = Arrays.asList("socket", "polling");
	protected static String defSocketNamespace = "plugins.MinecraftIntegration";

	JSONObject players = new JSONObject();

	private static boolean debug = false;

	public static JSONObject getPlayer(JSONObject req) {
		JSONObject res = new JSONObject();

			//final String name = playerName;

			//res.put("key", getForumAPIKey());
			//res.put("name", name);

			//if (playerData.isConfigurationSection(name + ".voted"))
			//{
				//HashMap<String,Object> vm = (HashMap<String,Object>)playerData.getConfigurationSection(name + ".voted").getValues(false);
				//res.put("votes", vm);
			//}

		return res;
	}

	public static JSONObject getPlayerVotes(JSONObject req) {
		////if (playerData == null) reload();

		JSONObject res = new JSONObject();

		try {
			final String name = req.getString("name");

			//res.put("key", getForumAPIKey());
			res.put("name", name);

			//if (playerData.isConfigurationSection(name + ".voted"))
			//{
			//	HashMap<String,Object> vm = (HashMap<String,Object>)playerData.getConfigurationSection(name + ".voted").getValues(false);
			//	res.put("votes", vm);
			//}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return res;
	}

	public static boolean getDebug() { return debug; }

	public static JSONObject setPlayerVotes(JSONObject req) {
		return new JSONObject();
	}

	public static void setDebug(boolean _debug) { debug = _debug; }

	public enum ConfigKey {
		FORUMURL("Forum URL"),
		FORUMNAME("Forum Name"),
		FORUMAPIKEY("Forum API Key"),

		SOCKETADDRESS("SocketIO Address"),
		SOCKETTRANSPORTS("SocketIO Transports"),
		SOCKETNAMESPACE("SocketIO Namespace");

		private ConfigCategory category;
		private String key;
		private String comment;

		public String getCategory() {
			return category.getName();
		}
		public String getKey() {
			return key;
		}
		public String getComment() {
			return comment;
		}

		ConfigKey(String key) {
			this.category = ConfigCategory.GENERAL;
			this.key = key;
			this.comment = "";
		}

		ConfigKey(String key, String comment) {
			this.category = ConfigCategory.GENERAL;
			this.key = key;
			this.comment = comment;
		}

		ConfigKey(ConfigCategory category, String key) {
			this.category = category;
			this.key = key;
			this.comment = "";
		}

		ConfigKey(ConfigCategory category, String key, String comment) {
			this.category = category;
			this.key = key;
			this.comment = comment;
		}
	}

	private enum ConfigCategory {
		GENERAL("General");

		public String getName() {
			return name;
		}

		private String name;

		ConfigCategory(String name) {
			this.name = name;
		}
	}
}
