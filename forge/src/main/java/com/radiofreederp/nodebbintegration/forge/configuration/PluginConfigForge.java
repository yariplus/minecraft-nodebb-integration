package com.radiofreederp.nodebbintegration.forge.configuration;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.configuration.IPluginConfig;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;
import com.radiofreederp.nodebbintegration.utils.Logger;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PluginConfigForge extends PluginConfig implements IPluginConfig {
	public PluginConfigForge(Object... args) { super(args); }

	private Configuration configuration;

	//@Override
	public void init(Object... args) {
		configuration = new Configuration((File)args[0]);

		try {
			configuration.load();


		} catch (Exception e) {
			Logger.log("Exception loading configuration:");
			e.printStackTrace();
		} finally {
			configuration.save();
		}
	}

	//@Override
	public void reload() {

	}

	//@Override
	public void save() {

	}

	public String getForumURL() { return configuration.get(ConfigKey.FORUMURL.getCategory(), ConfigKey.FORUMURL.getKey(), defForumUrl).getString(); }
	public String getForumName() { return configuration.get(ConfigKey.FORUMNAME.getCategory(), ConfigKey.FORUMNAME.getKey(), defForumName).getString(); }
	public String getForumAPIKey() { return configuration.get(ConfigKey.FORUMAPIKEY.getCategory(), ConfigKey.FORUMAPIKEY.getKey(), defForumAPIKey).getString(); }

	public String getSocketAddress() { return configuration.get(ConfigKey.SOCKETADDRESS.getCategory(), ConfigKey.SOCKETADDRESS.getKey(), defSocketAddress).getString(); }
	public List<String> getSocketTransports() { return Arrays.asList(configuration.get(ConfigKey.SOCKETTRANSPORTS.getCategory(), ConfigKey.SOCKETTRANSPORTS.getKey(), (String[])defSocketTransports.toArray()).getStringList()); }
	public String getSocketNamespace() { return configuration.get(ConfigKey.SOCKETNAMESPACE.getCategory(), ConfigKey.SOCKETNAMESPACE.getKey(), defSocketNamespace).getString(); }

	public void setForumURL(String url) { configuration.get(ConfigKey.FORUMURL.getCategory(), ConfigKey.FORUMURL.getKey(), defForumUrl).set(url); }
	public void setForumName(String name) { configuration.get(ConfigKey.FORUMNAME.getCategory(), ConfigKey.FORUMNAME.getKey(), defForumName).set(name); }
	public void setForumAPIKey(String key) { configuration.get(ConfigKey.FORUMNAME.getCategory(), ConfigKey.FORUMNAME.getKey(), defForumName).set(key); }

	public void setSocketAddress(String url) { configuration.get(ConfigKey.SOCKETADDRESS.getCategory(), ConfigKey.SOCKETADDRESS.getKey(), defSocketAddress).set(url); }
	public void setSocketTransports(List<String> transports) { configuration.get(ConfigKey.SOCKETTRANSPORTS.getCategory(), ConfigKey.SOCKETTRANSPORTS.getKey(), (String[])defSocketTransports.toArray()).set((String[])transports.toArray()); }
	public void setSocketNamespace(String name) { configuration.get(ConfigKey.SOCKETNAMESPACE.getCategory(), ConfigKey.SOCKETNAMESPACE.getKey(), defSocketNamespace).set(name); }

	public String getSocketTransportsAsString() {
		return "[" + String.join(",", getSocketTransports()) + "]";
	}
}
