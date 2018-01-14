package com.radiofreederp.nodebbintegration.forge.listeners;

import com.radiofreederp.nodebbintegration.MinecraftServerEvents;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.listeners.ListenerNodeBBIntegration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Created by Yari on 6/28/2017.
 */
public class ListenerPlayerJoin extends ListenerNodeBBIntegration {
	public ListenerPlayerJoin(NodeBBIntegrationPlugin plugin) {
		super(plugin);
	}

	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.entity;

			MinecraftServerEvents.onPlayerJoin(player, plugin.getMinecraftServer().getPlayerJSON(player));
		}
	}
}
