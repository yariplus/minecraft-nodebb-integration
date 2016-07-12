package com.radiofreederp.nodebbintegration.bukkit.hooks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import me.edge209.OnTime.OnTimeAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

public final class OnTimeHook {

    private OnTimeHook(){}

    protected static boolean enabled = false;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void hook(NodeBBIntegrationBukkit plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("OnTime")) {
            enabled = true;
            NodeBBIntegrationBukkit.instance.log("Hooked into OnTime.");
        }
    }

    public static void onTimeCheckTime(Player player, JSONObject obj) throws JSONException {
        if (OnTimeAPI.playerHasOnTimeRecord(player.getName())) {
            obj.put("playtime", OnTimeAPI.getPlayerTimeData(player.getName(), OnTimeAPI.data.TOTALPLAY));
        }
    }
}
