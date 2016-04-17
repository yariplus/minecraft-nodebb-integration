package com.radiofreederp.nodebbintegration.bukkit.listeners;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by Yari on 12/29/2015.
 */
public class ListenerVotifier implements Listener
{
    @EventHandler(priority=EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event)
    {
        Vote vote = event.getVote();
        String service = vote.getServiceName().replace('.', '_');
        String timestamp = vote.getTimeStamp();

        ((YamlConfiguration)NodeBBIntegrationBukkit.instance.getPluginConfig().getPlayerData()).set(vote.getUsername() + ".voted." + service, timestamp);
    }
}
