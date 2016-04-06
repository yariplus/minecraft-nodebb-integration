package com.radiofreederp.nodebbintegration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Yari on 1/4/2016.
 */
public class PlayerManager
{
	private static File file;
	private static YamlConfiguration playerData;

	private PlayerManager() {}

	private static PlayerManager playerManager;

	public static YamlConfiguration getPlayerData()
	{
		if (playerData == null) reloadConfig();

		return playerData;
	}

	public static void reloadConfig()
	{
		file = new File(NodeBBIntegrationBukkit.instance.getDataFolder(), "players.yml");
		playerData = YamlConfiguration.loadConfiguration(file);
	}

	public static void saveConfig() {
		NodeBBIntegrationBukkit.log("Saving player data.");
		try {
			playerData.save(file);
		} catch (IOException ex) {
			NodeBBIntegrationBukkit.log("Could not save player data to " + file.getName(), Level.SEVERE);
			ex.printStackTrace();
		}
	}
}
