package com.yaricraft.nodebbintegration.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
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
        NodeBBIntegration.log(vote.getUsername());
        NodeBBIntegration.log(vote.getAddress());
        NodeBBIntegration.log(vote.getServiceName());
        NodeBBIntegration.log(vote.getTimeStamp());
    }
}
