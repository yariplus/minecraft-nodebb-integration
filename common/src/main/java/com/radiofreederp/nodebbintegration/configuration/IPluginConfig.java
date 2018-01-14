package com.radiofreederp.nodebbintegration.configuration;

import java.util.List;

public interface IPluginConfig {
	void init(Object... args);
	void reload();
	void save();

	String getForumURL();
	String getForumName();
	String getForumAPIKey();

	String getSocketAddress();
	List<String> getSocketTransports();
	String getSocketNamespace();

	void setForumURL(String url);
	void setForumName(String name);
	void setForumAPIKey(String key);

	void setSocketAddress(String url);
	void setSocketTransports(List<String> transports);
	void setSocketNamespace(String name);

	String getSocketTransportsAsString();
}
