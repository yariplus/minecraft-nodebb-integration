package com.radiofreederp.nodebbintegration.forge.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.listeners.ListenerNodeBBIntegration;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class ListenerPlayerQuit extends ListenerNodeBBIntegration {
	public ListenerPlayerQuit(NodeBBIntegrationPlugin plugin) {
		super(plugin);
	}

	public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.player;

			MinecraftServerEvents.onPlayerQuit(player, plugin.getMinecraftServer().getPlayerJSON(player));
		}
	}
}
