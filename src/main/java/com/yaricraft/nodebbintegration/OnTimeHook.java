package com.yaricraft.nodebbintegration;

import me.edge209.OnTime.OnTimeAPI;

import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;


public class OnTimeHook {
    
    // OnTime
    public static boolean ontime = false;
    
    public static void hook() {
        OnTimeAPI.data.values();
        ontime = true;
        NodeBBIntegration.log("OnTime found.");
    }
    
    public static void onTimeCheckTime(Player player, JSONObject obj) throws JSONException {
        if (OnTimeAPI.playerHasOnTimeRecord(player.getName())) {
            obj.put("playtime", OnTimeAPI.getPlayerTimeData(player.getName(), OnTimeAPI.data.TOTALPLAY));
        }
    }
}
