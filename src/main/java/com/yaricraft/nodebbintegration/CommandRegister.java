package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandRegister implements CommandExecutor {
    private final NodeBBIntegration plugin;

    public CommandRegister(NodeBBIntegration plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        final CommandSender commandSender = sender;
        final String forumname;
        final String forumurl;

        // Get config
        try {
            forumname = plugin.getConfig().getString("FORUMNAME");
            forumurl  = plugin.getConfig().getString("FORUMURL");
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        // Sender needs to be a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command needs to be run by a player.");
            return true;
        }

        // Alert
        sender.sendMessage("Registering your player on " + p(forumname) + " (" + forumurl + ")");

        // If we're not connected, don't do anything.
        if (SocketIOClient.getSocket() == null || !SocketIOClient.getSocket().connected()) {
            sender.sendMessage("Sorry! The server isn't currently connected to the forum.");
            return true;
        }

        // Assert parameters.
        if (args.length != 2) {
            sender.sendMessage("Error: Please use \"/register [email] [password]\"");
            return true;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("email", args[0]);
            obj.put("password", args[1]);
            obj.put("name", sender.getName());
            obj.put("id", ((Player) sender).getUniqueId().toString());
            obj.put("key", plugin.getConfig().getString("APIKEY"));
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }

        // DEBUG
        NodeBBIntegration.log("Sending " + SocketIOClient.getNamespace() + "commandRegister");

        SocketIOClient.getSocket().emit(SocketIOClient.getNamespace() + "commandRegister", obj, new Ack() {
            @Override
            public void call(Object... args) {

                String result;
                String message;

                try {
                    if (args[0] != null) {
                        result = ((JSONObject)args[0]).getString("message");
                    }else{
                        result = ((JSONObject)args[1]).getString("result");
                    }
                }catch (Exception e) {
                    result = "BADRES";
                }

                if (result.equals("REGISTER")) {
                    message = "Success! Registered your player on " + p(forumname);
                } else if (result.equals("CREATE")) {
                    message = "Success! Created a new account on " + p(forumname);
                } else if (result.equals("FAILPASS")) {
                    message = "Invalid Password";
                } else if (result.equals("FAILDB")) {
                    message = "Error on forum. Please inform an administrator.";
                } else if (result.equals("FAILEMAIL")) {
                    message = "Invalid email.";
                } else if (result.equals("BADRES")) {
                    message = "Internal error. Please inform an administrator.";
                } else if (result.equals("FAILDATA")) {
                    message = "Data error. Please inform an administrator.";
                } else {
                    message = "Parsing error. Please inform an administrator.";
                }

                commandSender.sendMessage(message);
            }
        });

        return true;
    }

    private String p(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
