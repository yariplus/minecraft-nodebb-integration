package com.yaricraft.nodebbintegration;

import com.github.nkzawa.emitter.Emitter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskRegister extends BukkitRunnable
{
    public TaskRegister(CommandSender commandSender, String[] strings) {
        this.commandSender = commandSender;
        this.strings = strings;
    }

    CommandSender commandSender;
    String[] strings;

    @Override
    public void run() {
        if (SocketIOClient.getSocket() == null) return;

        try {
            commandSender.sendMessage("Registering you on " + NodeBBIntegration.getPlugin(JavaPlugin.class).getConfig().getString("FORUMNAME") + "...");
        }catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", strings[0]);
            obj.put("password", strings[1]);
            obj.put("name", commandSender.getName());
            obj.put("id", ((Player) commandSender).getUniqueId().toString());
            obj.put("key", NodeBBIntegration.getPlugin(JavaPlugin.class).getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Sending " + SocketIOClient.getNamespace() + "CommandRegister");

        SocketIOClient.getSocket().emit(SocketIOClient.getNamespace() + "CommandRegister", obj, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null) {
                    System.out.println("CommandRegister received.");
                }else{
                    System.out.println("CommandRegister error: ");
                }
            }
        });
    }
}
