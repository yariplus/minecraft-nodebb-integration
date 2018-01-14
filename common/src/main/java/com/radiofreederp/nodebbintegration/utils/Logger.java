package com.radiofreederp.nodebbintegration.utils;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.configuration.PluginConfig;

import java.util.HashMap;
import java.util.logging.Level;

public final class Logger {
	private Logger() {}

	private static NodeBBIntegrationPlugin plugin;

	public static void log(String message) { Logger.log(message, Level.INFO); }
	public static void info(String message) { Logger.log(message, Level.INFO); }
	public static void error(String message) {Logger.log(message, Level.SEVERE); }
	public static void log(String message, Level level) {
		// Only log if there is a severe error or if the plugin is in debug mode.
		if (level == Level.SEVERE || PluginConfig.getDebug()) plugin.log(message, level);
	}

	public static void init(NodeBBIntegrationPlugin _plugin) {
		plugin = _plugin;

		tol4j.put(Level.SEVERE, "ERROR");
		tol4j.put(Level.WARNING, "WARN");
		tol4j.put(Level.INFO, "INFO");
		tol4j.put(Level.FINE, "DEBUG");
	}

	public static HashMap<Level, String> tol4j = new HashMap<Level, String>();
}
