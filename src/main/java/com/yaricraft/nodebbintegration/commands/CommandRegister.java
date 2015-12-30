package com.yaricraft.nodebbintegration.commands;

import com.github.nkzawa.socketio.client.Ack;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.SocketIOClient;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Yari on 10/5/2015.
 */
public class CommandRegister implements CommandExecutor {
    private final NodeBBIntegration plugin;

    public CommandRegister(NodeBBIntegration plugin) { this.plugin = plugin; }

    // Since I can't use getConfig up here, I've set these to null so that I can reduce disk IO later.
    List<String> RegisterAlert = null;
    List<String> RegisterNotConnected = null;
    List<String> RegisterAssertParameters = null;
    List<String> RegisterRegSuccess = null;
    List<String> RegisterCreatedNewAccount = null;
    List<String> RegisterFailPass = null;
    List<String> RegisterFailDB = null;
    List<String> RegisterFailEmail = null;
    List<String> RegisterBadRes = null;
    List<String> RegisterFailData = null;
    List<String> RegisterParsingError = null;
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
        if (RegisterAlert == null) {
            RegisterAlert = plugin.getConfig().getStringList("PluginMessages.Register.Alert");
        }
        for (String str : RegisterAlert) {
            str = str.replaceAll("%forumname%", forumname);
            str = str.replaceAll("%forumurl%",forumurl);
            sender.sendMessage(p(str));
        }

        // If we're not connected, don't do anything.
        if (SocketIOClient.getSocket() == null || !SocketIOClient.getSocket().connected()) {
            if (RegisterNotConnected == null) {
                RegisterNotConnected = plugin.getConfig().getStringList("PluginMessages.Register.NotConnected");
            }
            for (String str : RegisterNotConnected) {
                sender.sendMessage(p(str));
            }
            return true;
        }

        // Assert parameters.
        if (args.length != 2) {
            if (RegisterAssertParameters == null) {
                RegisterAssertParameters = plugin.getConfig().getStringList("PluginMessages.Register.AssertParameters");
            }
            for (String str : RegisterAssertParameters) {
                sender.sendMessage(p(str));
            }
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
                List<String> message;

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
                    if (RegisterRegSuccess == null) {
                        RegisterRegSuccess = plugin.getConfig().getStringList("PluginMessages.Register.RegSuccess");
                    }
                    message = RegisterRegSuccess;
                } else if (result.equals("CREATE")) {
                    if (RegisterCreatedNewAccount == null) {
                        RegisterCreatedNewAccount = plugin.getConfig().getStringList("PluginMessages.Register.CreatedNewAccount");
                    }
                    message = RegisterCreatedNewAccount;
                } else if (result.equals("FAILPASS")) {
                    if (RegisterFailPass == null) {
                        RegisterFailPass = plugin.getConfig().getStringList("PluginMessages.Register.FailPass");
                    }
                    message = RegisterFailPass;
                } else if (result.equals("FAILDB")) {
                    if (RegisterFailDB == null) {
                        RegisterFailDB = plugin.getConfig().getStringList("PluginMessages.Register.FailPass");
                    }
                    message = RegisterFailDB;
                } else if (result.equals("FAILEMAIL")) {
                    if (RegisterFailEmail == null) {
                        RegisterFailEmail = plugin.getConfig().getStringList("PluginMessages.Register.FailEmail");
                    }
                    message = RegisterFailEmail;
                } else if (result.equals("BADRES")) {
                    if (RegisterBadRes == null) {
                        RegisterBadRes = plugin.getConfig().getStringList("PluginMessages.Register.BadRes");
                    }
                    message = RegisterBadRes;
                } else if (result.equals("FAILDATA")) {
                    if (RegisterFailData == null) {
                        RegisterFailData = plugin.getConfig().getStringList("PluginMessages.Register.FailData");
                    }
                    message = RegisterFailData;
                } else {
                    if (RegisterParsingError == null) {
                        RegisterParsingError = plugin.getConfig().getStringList("PluginMessages.Register.ParsingError");
                    }
                    message = RegisterParsingError;
                }

                for (String str : message) {
                    str = str.replaceAll("%forumname%", forumname);
                    str = str.replaceAll("%forumurl%",forumurl);
                    commandSender.sendMessage(p(str));
                }
            }
        });

        return true;
    }

    private String p(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
