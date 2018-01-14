package com.radiofreederp.nodebbintegration.bukkit.configuration;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.configuration.IPluginConfig;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.utils.Logger;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class PluginConfigBukkit extends PluginConfig implements IPluginConfig {
	public PluginConfigBukkit(Object... args) { super(args); }

	private static File settingsFile;
	private static YamlConfiguration settingsData;

	private static File playerFile;
	private static YamlConfiguration playerData;

	private static String keyForumURL = ConfigKey.FORUMURL.getCategory() + "." + ConfigKey.FORUMURL.getKey();
	private static String keyForumName = ConfigKey.FORUMNAME.getCategory() + "." + ConfigKey.FORUMNAME.getKey();
	private static String keyForumAPIKey = ConfigKey.FORUMAPIKEY.getCategory() + "." + ConfigKey.FORUMAPIKEY.getKey();

	private static String keySocketAddress = ConfigKey.SOCKETADDRESS.getCategory() + '.' + ConfigKey.SOCKETADDRESS.getKey();
	private static String keySocketTransports = ConfigKey.SOCKETTRANSPORTS.getCategory() + "." + ConfigKey.SOCKETTRANSPORTS.getKey();
	private static String keySocketNamespace = ConfigKey.SOCKETNAMESPACE.getCategory() + "." + ConfigKey.SOCKETNAMESPACE.getKey();

	@Override
	public void init(Object... args) {
		try {
			// Create main settings.
			settingsFile = new File(((NodeBBIntegrationBukkit)NBBPlugin.instance).getDataFolder(), "settings.yml");
			settingsData = YamlConfiguration.loadConfiguration(settingsFile);

			// Create player data.
			playerFile = new File(((NodeBBIntegrationBukkit)NBBPlugin.instance).getDataFolder(), "players.yml");
			playerData = YamlConfiguration.loadConfiguration(playerFile);

			// Set in configuration.
			settingsData.set(keyForumURL, getForumURL() != null ? getForumURL() : defForumUrl);
			settingsData.set(keyForumName, getForumName() != null ? getForumName() : defForumName);
			settingsData.set(keyForumAPIKey, getForumAPIKey() != null ? getForumAPIKey() : defForumAPIKey);

			settingsData.set(keySocketAddress, getSocketAddress() != null ? getSocketAddress() : defSocketAddress);
			settingsData.set(keySocketTransports, getSocketTransports() != null ? getSocketTransports() : defSocketTransports);
			settingsData.set(keySocketNamespace, getSocketNamespace() != null ? getSocketNamespace() : defSocketNamespace);

			save();
		} catch (Exception e) {
			Logger.log("Exception loading configuration:");
			e.printStackTrace();
		} finally {
			Logger.log("Done loading configuration.");
		}
	}

	// Save configuration to disk.
	@Override
	public void save() {
		try {
			settingsData.save(settingsFile);
		} catch (IOException e) {
			Logger.log("Could not save settings data to " + settingsFile.getName(), Level.SEVERE);
			e.printStackTrace();
		}

		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			Logger.log("Could not save player data to " + playerFile.getName(), Level.SEVERE);
			e.printStackTrace();
		}
	}

	// Reload configuration from disk.
	@Override
	public void reload() {
		// Reinitialize configuration files.
		init();

		// Reconnect socket client with new settings.
		SocketIOClient.connect();
	}

	public String getForumURL() { return settingsData.getString(keyForumURL); }
	public String getForumName() { return settingsData.getString(keyForumName); }
	public String getForumAPIKey() { return settingsData.getString(keyForumAPIKey); }

	public String getSocketAddress() { return settingsData.getString(keySocketAddress); }
	public List<String> getSocketTransports() { return settingsData.getStringList(keySocketTransports); }
	public String getSocketNamespace() { return settingsData.getString(keySocketNamespace); }

	public void setForumURL(String url) { settingsData.set(keyForumURL, url); }
	public void setForumName(String name) { settingsData.set(keyForumName, name); }
	public void setForumAPIKey(String key) { settingsData.set(keyForumAPIKey, key); }

	public void setSocketAddress(String url) { settingsData.set(keySocketAddress, url); }
	public void setSocketTransports(List<String> transports) { settingsData.set(keySocketTransports, transports); }
	public void setSocketNamespace(String name) { settingsData.set(keySocketNamespace, name); }

	public String getSocketTransportsAsString() {
		return "[" + String.join(",", getSocketTransports()) + "]";
	}
}
