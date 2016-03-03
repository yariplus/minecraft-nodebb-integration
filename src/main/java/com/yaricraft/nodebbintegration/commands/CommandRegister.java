package com.yaricraft.nodebbintegration.commands;

import io.socket.client.Ack;
import com.yaricraft.nodebbintegration.NodeBBIntegration;
import com.yaricraft.nodebbintegration.socketio.SocketIOClient;
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
public class CommandRegister implements CommandExecutor
{
    protected static NodeBBIntegration plugin;

    public CommandRegister(NodeBBIntegration plugin) { CommandRegister.plugin = plugin; }

    // Since I can't use getConfig up here, I've set these to null so that I can reduce disk IO later.
    List<String> RegisterAlert = null;
    List<String> RegisterNotConnected = null;
    List<String> RegisterAssertParameters = null;
    List<String> RegisterParsingError = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
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
        if (SocketIOClient.disconnected()) {
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
        NodeBBIntegration.log("Sending commandRegister");

        SocketIOClient.emit("commandRegister", obj, new Ack() {
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

                RegisterResponse response = RegisterResponse.valueOf(result);

                if (response != null) {
                    message = response.getMessage();
                }
                else
                {
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

    private enum RegisterResponse
    {
        REGISTER  ("PluginMessages.Register.RegSuccess"),
        CREATE    ("PluginMessages.Register.CreatedNewAccount"),
        FAILPASS  ("PluginMessages.Register.FailPass"),
        FAILDB    ("PluginMessages.Register.FailPass"),
        FAILEMAIL ("PluginMessages.Register.FailEmail"),
        BADRES    ("PluginMessages.Register.BadRes"),
        FAILDATA  ("PluginMessages.Register.FailData"),
        ERROR     ("PluginMessages.Register.ParsingError");

        private String key;
        private List<String> message = null;

        RegisterResponse(String key) { this.key = key; }

        public List<String> getMessage() {
            if (this.message == null) this.message = plugin.getConfig().getStringList(this.key);
            return this.message;
        }
    }

    private String p(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
