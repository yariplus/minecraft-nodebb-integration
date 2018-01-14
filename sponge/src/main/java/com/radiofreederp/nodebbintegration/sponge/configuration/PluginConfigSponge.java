package com.radiofreederp.nodebbintegration.sponge.configuration;

import com.radiofreederp.nodebbintegration.configuration.IPluginConfig;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.socketio.SocketIOClient;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PluginConfigSponge extends PluginConfig implements IPluginConfig {
	public PluginConfigSponge(Object... args) { super(args); }

	private CommentedConfigurationNode configurationNode;
	private HoconConfigurationLoader configManager;

	private static String keyForumURL = ConfigKey.FORUMURL.getCategory() + "." + ConfigKey.FORUMURL.getKey();
	private static String keyForumName = ConfigKey.FORUMNAME.getCategory() + "." + ConfigKey.FORUMNAME.getKey();
	private static String keyForumAPIKey = ConfigKey.FORUMAPIKEY.getCategory() + "." + ConfigKey.FORUMAPIKEY.getKey();

	private static String keySocketAddress = ConfigKey.SOCKETADDRESS.getCategory() + '.' + ConfigKey.SOCKETADDRESS.getKey();
	private static String keySocketTransports = ConfigKey.SOCKETTRANSPORTS.getCategory() + "." + ConfigKey.SOCKETTRANSPORTS.getKey();
	private static String keySocketNamespace = ConfigKey.SOCKETNAMESPACE.getCategory() + "." + ConfigKey.SOCKETNAMESPACE.getKey();

	@Override
	public void init(Object... args) {
		Path privateConfigPath = (Path) args[0];
		Path configPath = Paths.get(privateConfigPath.toString(), NBBPlugin.ID + ".cfg");

		if (!Files.exists(configPath)) {
			try {
				Files.createDirectories(configPath.getParent());
				Files.createFile(configPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		configManager = HoconConfigurationLoader.builder().setPath(configPath).build();

		try {
			configurationNode = configManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set in configuration.
		configurationNode.getNode(keyForumURL).setValue(getForumURL() != null ? getForumURL() : defForumUrl);
		configurationNode.getNode(keyForumName).setValue(getForumName() != null ? getForumName() : defForumName);
		configurationNode.getNode(keyForumAPIKey).setValue(getForumAPIKey() != null ? getForumAPIKey() : defForumAPIKey);

		configurationNode.getNode(keySocketAddress).setValue(getSocketAddress() != null ? getSocketAddress() : defSocketAddress);
		configurationNode.getNode(keySocketTransports).setValue(getSocketTransports() != null && getSocketTransports().size() > 0 ? getSocketTransports() : defSocketTransports);
		configurationNode.getNode(keySocketNamespace).setValue(getSocketNamespace() != null ? getSocketAddress() : defSocketNamespace);

		save();
	}

	@Override
	public void reload() {
		try {
			configurationNode = configManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SocketIOClient.connect();
	}

	@Override
	public void save() {
		try {
			configManager.save(configurationNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getForumURL() { return configurationNode.getNode(keyForumURL).getString(); }
	public String getForumName() { return configurationNode.getNode(keyForumName).getString(); }
	public String getForumAPIKey() { return configurationNode.getNode(keyForumAPIKey).getString(); }

	public String getSocketAddress() { return configurationNode.getNode(keySocketAddress).getString(); }
	public List<String> getSocketTransports() { return configurationNode.getNode(keySocketTransports).getList(o -> (String)o); }
	public String getSocketNamespace() { return configurationNode.getNode(keySocketNamespace).getString(); }

	public void setForumURL(String url) { configurationNode.getNode(keyForumURL).setValue(url); }
	public void setForumName(String name) { configurationNode.getNode(keyForumName).setValue(name); }
	public void setForumAPIKey(String key) { configurationNode.getNode(keyForumAPIKey).setValue(key); }

	public void setSocketAddress(String url) { configurationNode.getNode(keySocketAddress).setValue(url); }
	public void setSocketTransports(List<String> transports) { configurationNode.getNode(keySocketTransports).setValue(transports); }
	public void setSocketNamespace(String name) { configurationNode.getNode(keySocketNamespace).setValue(name); }

	public String getSocketTransportsAsString() {
		return "[" + String.join(",", getSocketTransports()) + "]";
	}
}
