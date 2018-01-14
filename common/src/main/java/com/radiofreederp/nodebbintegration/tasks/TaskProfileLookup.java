package com.radiofreederp.nodebbintegration.tasks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;

public class TaskProfileLookup extends MinecraftTask implements Runnable {
	private NodeBBIntegrationPlugin plugin;
	private String username;
	private Runnable next;

	public TaskProfileLookup(NodeBBIntegrationPlugin plugin, String username, Runnable next) {
		this.plugin = plugin;
		this.username = username;
		this.next = next;
	}

	@Override
	public void run() {

	}
}
