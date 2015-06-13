package com.yaricraft.nodebbintegration;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Yari on 6/12/2015.
 */
public class TaskTick extends BukkitRunnable {

    private static TaskTick instance;

    private long timeLast;
    private String TPS;

    public TaskTick(JavaPlugin plugin){
        if (instance != null) instance.cancel();
        timeLast = System.currentTimeMillis();
        instance = this;
        instance.runTaskTimerAsynchronously(plugin, 20 * 60, 20 * 60);
    }

    @Override
    public void run() {
        long timeNow = System.currentTimeMillis();
        double ticks = 20D / ( ((double)( timeNow - timeLast )) / 60000D );
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        TPS = df.format(ticks);
        timeLast = timeNow;
    }

    public static String getTPS() {
        return instance.TPS;
    }
}
